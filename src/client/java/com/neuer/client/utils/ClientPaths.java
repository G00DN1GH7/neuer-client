package com.neuer.client.utils;

import java.nio.file.Path;

/**
 * Resolves the client's on-disk storage locations without touching Minecraft classes,
 * so storage logic can be unit-tested outside a running game.
 *
 * <p>Resolution order: an explicitly configured base directory (the game directory at
 * runtime), then the {@code neuer.config.dir} system property, then
 * {@code ./config/neuerclient}.</p>
 */
public final class ClientPaths {
    public static final String CONFIG_DIR_PROPERTY = "neuer.config.dir";

    private static volatile Path baseDirectory;

    private ClientPaths() {
    }

    public static void setBaseDirectory(Path directory) {
        baseDirectory = directory;
    }

    public static Path baseDirectory() {
        Path configured = baseDirectory;
        if (configured != null) {
            return configured;
        }
        String property = System.getProperty(CONFIG_DIR_PROPERTY);
        if (property != null && !property.isBlank()) {
            return Path.of(property);
        }
        return Path.of("config", "neuerclient");
    }

    public static Path resolve(String fileName) {
        return baseDirectory().resolve(fileName);
    }
}
