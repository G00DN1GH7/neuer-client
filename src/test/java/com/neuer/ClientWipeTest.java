package com.neuer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.neuer.client.utils.ClientWipe;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ClientWipeTest {

    @TempDir
    Path tempDir;

    @Test
    void removesClientDirectoriesAndTempArtefactsOnly() throws Exception {
        Path clientDir = tempDir.resolve("config/neuerclient");
        Path configs = clientDir.resolve("configs");
        Files.createDirectories(configs);
        Files.writeString(clientDir.resolve("config.enc"), "encrypted");
        Files.writeString(clientDir.resolve("config.key"), "key");
        Files.writeString(configs.resolve("LegitSword.json"), "{}");

        Path cacheDir = tempDir.resolve("perf-cache");
        Files.createDirectories(cacheDir);
        Files.writeString(cacheDir.resolve("cache.bin"), "x");

        Path tmp = tempDir.resolve("tmp");
        Files.createDirectories(tmp);
        Files.writeString(tmp.resolve("mc_ec_artifact.tmp"), "trace");
        Files.writeString(tmp.resolve("unrelated.txt"), "keep me");

        Path unrelated = tempDir.resolve("other/file.txt");
        Files.createDirectories(unrelated.getParent());
        Files.writeString(unrelated, "keep me too");

        int removed = ClientWipe.purge(List.of(clientDir, cacheDir), tmp);

        assertFalse(Files.exists(clientDir), "client config directory must be gone");
        assertFalse(Files.exists(cacheDir), "hidden cache directory must be gone");
        assertFalse(Files.exists(tmp.resolve("mc_ec_artifact.tmp")), "temp artefact must be gone");
        assertTrue(Files.exists(tmp.resolve("unrelated.txt")), "unrelated temp files must survive");
        assertTrue(Files.exists(unrelated), "unrelated files must survive");
        assertTrue(removed >= 6, "expected at least six removed paths, got " + removed);
    }

    @Test
    void ignoresMissingPaths() {
        assertEquals(0, ClientWipe.purge(List.of(tempDir.resolve("does/not/exist")), null));
    }

    @Test
    void refusesHomeAndFilesystemRoots() {
        Path home = Path.of(System.getProperty("user.home"));

        assertEquals(0, ClientWipe.purge(List.of(home), null), "must never delete the home directory");
        assertTrue(Files.exists(home));
        assertEquals(0, ClientWipe.purge(List.of(Path.of("/")), null), "must never delete the filesystem root");
    }

    @Test
    void wipedFlagBlocksPersistence() {
        assertFalse(ClientWipe.isWiped(), "flag must start clear");
        ClientWipe.markWiped();
        assertTrue(ClientWipe.isWiped());
    }
}
