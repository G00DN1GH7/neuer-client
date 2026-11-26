package com.neuer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.neuer.client.utils.CryptoUtils;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class CryptoUtilsTest {
    /** Key and IV of configs written before the per-install key existed. */
    private static final String LEGACY_KEY = "EclipseWareConfigEncKey_32bytes!";
    private static final String LEGACY_IV = "EclipseWareIV_16";

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        CryptoUtils.setKeyDirectory(tempDir);
    }

    @Test
    void roundTripsThroughPerInstallKey() {
        String payload = "{\"Modules\":[],\"ConfigVersion\":2}";

        String encrypted = CryptoUtils.encrypt(payload);

        assertNotNull(encrypted);
        assertTrue(encrypted.startsWith(CryptoUtils.PREFIX));
        assertFalse(CryptoUtils.isLegacy(encrypted));
        assertEquals(payload, CryptoUtils.decrypt(encrypted));
    }

    @Test
    void usesFreshIvForEveryEncryption() {
        String first = CryptoUtils.encrypt("same payload");
        String second = CryptoUtils.encrypt("same payload");

        assertNotNull(first);
        assertNotNull(second);
        assertFalse(first.equals(second), "identical plaintext must not produce identical ciphertext");
        assertEquals("same payload", CryptoUtils.decrypt(second));
    }

    @Test
    void decryptsLegacyPayloadsWithTheStaticKey() throws Exception {
        String legacy = encryptLegacy("{\"Modules\":[{\"Name\":\"Sprint\"}]}");

        assertTrue(CryptoUtils.isLegacy(legacy));
        assertNull(CryptoUtils.encrypt(null));
        assertEquals("{\"Modules\":[{\"Name\":\"Sprint\"}]}", CryptoUtils.decrypt(legacy));
    }

    @Test
    void returnsNullInsteadOfThrowingOnBadPayloads() {
        assertNull(CryptoUtils.decrypt(null));
        assertNull(CryptoUtils.decrypt("   "));
        assertNull(CryptoUtils.decrypt(CryptoUtils.PREFIX + "not-base64!!"));
        assertNull(CryptoUtils.decrypt("this-is-not-encrypted-data"));
        assertNull(CryptoUtils.decrypt(""));
    }

    @Test
    void rejectsPayloadsEncryptedWithAnotherInstallKey() {
        CryptoUtils.setKeyDirectory(tempDir.resolve("install-a"));
        String encrypted = CryptoUtils.encrypt("secret");
        Path foreignKey = tempDir.resolve("install-b");
        CryptoUtils.setKeyDirectory(foreignKey);

        assertNotNull(encrypted);
        assertNull(CryptoUtils.decrypt(encrypted));
    }

    @Test
    void keepsTheGeneratedKeyFileStableAcrossReloads() throws Exception {
        String encrypted = CryptoUtils.encrypt("persisted");
        Path keyFile = CryptoUtils.keyFile();

        assertTrue(Files.exists(keyFile));
        assertEquals(32, Files.size(keyFile));

        byte[] before = Files.readAllBytes(keyFile);
        CryptoUtils.setKeyDirectory(tempDir);
        assertEquals("persisted", CryptoUtils.decrypt(encrypted));
        assertTrue(java.util.Arrays.equals(before, Files.readAllBytes(keyFile)));
    }

    @Test
    void reportsACorruptKeyFileInsteadOfSilentlyRotatingIt() throws Exception {
        Files.writeString(CryptoUtils.keyFile(), "too-short", StandardCharsets.UTF_8);

        assertNull(CryptoUtils.encrypt("payload"));
        assertEquals("too-short", Files.readString(CryptoUtils.keyFile(), StandardCharsets.UTF_8));
    }

    private static String encryptLegacy(String value) throws Exception {
        IvParameterSpec iv = new IvParameterSpec(LEGACY_IV.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec keySpec = new SecretKeySpec(LEGACY_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
        return Base64.getEncoder().encodeToString(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
    }
}
