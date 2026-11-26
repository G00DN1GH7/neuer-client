package com.neuer.client.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Set;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Config encryption.
 *
 * <p>New payloads are prefixed with {@link #PREFIX} and encrypted with AES-256-GCM using a
 * per-install key stored in {@code config.key} next to the config file, with a fresh random
 * IV for every encryption. Payloads without the prefix are legacy (static key, static IV)
 * and are still decrypted so existing config files, clipboard strings and bundled
 * recommended configs keep working.</p>
 */
public final class CryptoUtils {
    /** Marker for payloads encrypted with the per-install key. */
    public static final String PREFIX = "neuer1:";

    private static final String LEGACY_KEY = "EclipseWareConfigEncKey_32bytes!";
    private static final String LEGACY_IV = "EclipseWareIV_16";

    private static final String KEY_FILE_NAME = "config.key";
    private static final int KEY_BYTES = 32;
    private static final int IV_BYTES = 12;
    private static final int TAG_BITS = 128;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static volatile byte[] installKey;

    private CryptoUtils() {
    }

    /** Points key storage at the game's config directory. */
    public static void setKeyDirectory(Path directory) {
        synchronized (CryptoUtils.class) {
            ClientPaths.setBaseDirectory(directory);
            installKey = null;
        }
    }

    public static Path keyFile() {
        return ClientPaths.resolve(KEY_FILE_NAME);
    }

    /** True when the payload predates the per-install key. */
    public static boolean isLegacy(String payload) {
        return payload != null && !payload.startsWith(PREFIX);
    }

    public static String encrypt(String value) {
        if (value == null) {
            return null;
        }
        try {
            byte[] iv = new byte[IV_BYTES];
            RANDOM.nextBytes(iv);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(installKey(), "AES"), new GCMParameterSpec(TAG_BITS, iv));
            byte[] cipherText = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

            byte[] payload = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, payload, 0, iv.length);
            System.arraycopy(cipherText, 0, payload, iv.length, cipherText.length);

            return PREFIX + Base64.getEncoder().encodeToString(payload);
        } catch (Exception ex) {
            System.err.println("[Neuer] Config encryption failed: " + ex.getMessage());
            return null;
        }
    }

    public static String decrypt(String encrypted) {
        if (encrypted == null || encrypted.isBlank()) {
            return null;
        }
        if (encrypted.startsWith(PREFIX)) {
            return decryptWithInstallKey(encrypted.substring(PREFIX.length()));
        }
        return decryptLegacy(encrypted);
    }

    private static String decryptWithInstallKey(String payload) {
        try {
            byte[] raw = Base64.getDecoder().decode(payload.trim());
            if (raw.length <= IV_BYTES) {
                return null;
            }
            byte[] iv = Arrays.copyOfRange(raw, 0, IV_BYTES);
            byte[] cipherText = Arrays.copyOfRange(raw, IV_BYTES, raw.length);

            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(installKey(), "AES"), new GCMParameterSpec(TAG_BITS, iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception ex) {
            System.err.println("[Neuer] Config decryption failed (wrong key or modified payload?): " + ex.getMessage());
            return null;
        }
    }

    private static String decryptLegacy(String payload) {
        try {
            IvParameterSpec iv = new IvParameterSpec(LEGACY_IV.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec keySpec = new SecretKeySpec(LEGACY_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(payload.trim()));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            System.err.println("[Neuer] Config decryption failed (legacy payload, maybe modified manually?): " + ex.getMessage());
            return null;
        }
    }

    private static byte[] installKey() throws IOException {
        byte[] cached = installKey;
        if (cached != null) {
            return cached;
        }
        synchronized (CryptoUtils.class) {
            if (installKey != null) {
                return installKey;
            }
            Path file = keyFile();
            byte[] key = Files.exists(file) ? readKey(file) : createKey(file);
            installKey = key;
            return key;
        }
    }

    private static byte[] readKey(Path file) throws IOException {
        byte[] key = Files.readAllBytes(file);
        if (key.length != KEY_BYTES) {
            throw new IOException("Invalid key length in " + file + " (expected " + KEY_BYTES + " bytes, found "
                    + key.length + "). Delete the file to generate a new key; existing configs will no longer decrypt.");
        }
        return key;
    }

    private static byte[] createKey(Path file) throws IOException {
        byte[] key = new byte[KEY_BYTES];
        RANDOM.nextBytes(key);
        Path parent = file.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        Files.write(file, key);
        restrictPermissions(file);
        return key;
    }

    private static void restrictPermissions(Path file) {
        try {
            Files.setPosixFilePermissions(file, Set.of(
                    PosixFilePermission.OWNER_READ,
                    PosixFilePermission.OWNER_WRITE));
        } catch (UnsupportedOperationException | IOException ignored) {
            // Windows and other non-POSIX filesystems: keep the default permissions.
        }
    }
}
