package com.neuer.client.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import com.neuer.client.utils.cheat.ScreenshareBypass;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Removes everything the client leaves on disk. Used by the self-destruct key.
 *
 * <p>Only paths owned by the client are touched: the config directory
 * ({@code config/neuerclient}), the hidden screenshare cache directory and temp artefacts written
 * with the {@code mc_ec_} prefix. The mod jar is never touched while the game runs — deleting a
 * loaded jar breaks resource loading on Linux and fails on Windows — it is queued with
 * {@link #scheduleModFileRemovalAtShutdown()} and removed from a JVM shutdown hook instead.
 * Once a wipe has run, {@link #isWiped()} makes the persistence helpers refuse to recreate
 * the files.</p>
 */
public final class ClientWipe {
    private static final String TEMP_ARTIFACT_PREFIX = "mc_ec_";

    private static volatile boolean wiped;
    private static volatile boolean jarRemovalScheduled;

    private ClientWipe() {
    }

    public static boolean isWiped() {
        return wiped;
    }

    public static void markWiped() {
        wiped = true;
    }

    public static int purgeAll() {
        markWiped();
        List<Path> roots = new ArrayList<>();
        roots.add(ClientPaths.baseDirectory());
        roots.add(ScreenshareBypass.safeConfigPath());
        return purge(roots, Path.of(System.getProperty("java.io.tmpdir", ".")));
    }

    /**
     * Deletes every file below {@code roots} plus temp artefacts, without ever touching an
     * unrelated directory. Returns the number of files and directories removed.
     */
    public static int purge(List<Path> roots, Path tempDirectory) {
        int removed = 0;
        if (roots != null) {
            for (Path root : roots) {
                removed += deleteTree(root);
            }
        }
        removed += deleteTempArtefacts(tempDirectory);
        return removed;
    }

    private static int deleteTree(Path root) {
        if (root == null) {
            return 0;
        }
        Path absolute;
        try {
            absolute = root.toAbsolutePath().normalize();
        } catch (RuntimeException e) {
            return 0;
        }
        if (!Files.exists(absolute)) {
            return 0;
        }
        if (isUnsafeRoot(absolute)) {
            System.err.println("[Neuer] Refusing to delete unsafe path during self destruct: " + absolute);
            return 0;
        }

        int removed = 0;
        try (Stream<Path> walk = Files.walk(absolute)) {
            List<Path> entries = walk.sorted(Comparator.reverseOrder()).toList();
            for (Path entry : entries) {
                try {
                    if (Files.deleteIfExists(entry)) {
                        removed++;
                    }
                } catch (IOException e) {
                    System.err.println("[Neuer] Could not delete " + entry + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[Neuer] Could not walk " + absolute + ": " + e.getMessage());
        }
        return removed;
    }

    private static boolean isUnsafeRoot(Path absolute) {
        if (absolute.getParent() == null || absolute.getNameCount() < 2) {
            return true;
        }
        String homeProperty = System.getProperty("user.home");
        if (homeProperty == null || homeProperty.isBlank()) {
            return false;
        }
        Path home = Path.of(homeProperty).toAbsolutePath().normalize();
        return absolute.equals(home) || home.startsWith(absolute);
    }

    private static int deleteTempArtefacts(Path tempDirectory) {
        if (tempDirectory == null || !Files.isDirectory(tempDirectory)) {
            return 0;
        }
        int removed = 0;
        try (Stream<Path> entries = Files.list(tempDirectory)) {
            for (Path entry : entries.toList()) {
                if (entry.getFileName() != null
                        && entry.getFileName().toString().startsWith(TEMP_ARTIFACT_PREFIX)
                        && Files.isRegularFile(entry)) {
                    try {
                        if (Files.deleteIfExists(entry)) {
                            removed++;
                        }
                    } catch (IOException e) {
                        System.err.println("[Neuer] Could not delete temp artefact " + entry + ": " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[Neuer] Could not list temp directory: " + e.getMessage());
        }
        return removed;
    }

    /**
     * Removes the mod jar from a shutdown hook, after the game has stopped reading it. Doing this
     * while the client is loading resources breaks Linux and fails on Windows, so it is deferred.
     * Safe to call more than once; only the first call installs a hook.
     */
    public static void scheduleModFileRemovalAtShutdown() {
        if (jarRemovalScheduled) {
            return;
        }
        jarRemovalScheduled = true;
        Thread hook = new Thread(() -> {
            if (!wiped) {
                return;
            }
            int removed = deleteLoadedModFiles();
            if (removed == 0) {
                System.err.println("[Neuer] Could not remove the mod jar while the game was running; delete it by hand after closing Minecraft.");
            }
        }, "NeuerSelfDestructJarCleanup");
        try {
            Runtime.getRuntime().addShutdownHook(hook);
        } catch (IllegalStateException alreadyShuttingDown) {
            hook.run();
        }
    }

    /** Best effort: the JVM can still hold the jar open, especially on Windows. */
    private static int deleteLoadedModFiles() {
        int removed = 0;
        try {
            var container = FabricLoader.getInstance().getModContainer("neuerclient");
            if (container.isEmpty()) {
                return 0;
            }
            for (Path path : container.get().getOrigin().getPaths()) {
                if (path == null || !Files.isRegularFile(path)) {
                    continue;
                }
                try {
                    if (Files.deleteIfExists(path)) {
                        removed++;
                    }
                } catch (IOException e) {
                    System.err.println("[Neuer] Could not delete mod file " + path + ": " + e.getMessage());
                }
            }
        } catch (Throwable t) {
            System.err.println("[Neuer] Could not resolve the mod file for self destruct: " + t.getMessage());
        }
        return removed;
    }
}
