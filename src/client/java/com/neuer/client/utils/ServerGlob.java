package com.neuer.client.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Server address matching shared by the recommended-configs and detection-database modules.
 *
 * <p>Patterns are plain addresses or a {@code *} suffix wildcard ({@code *example.com}).
 * A pattern without a port also matches the same address with a port ({@code play.example.com:25565}).</p>
 */
public final class ServerGlob {
    private ServerGlob() {
    }

    public static boolean matches(String glob, String serverAddress) {
        if (glob == null || serverAddress == null) {
            return false;
        }
        String pattern = normalize(glob);
        if (pattern.isEmpty()) {
            return false;
        }
        String address = normalize(serverAddress);
        String host = hostOf(address);

        if (pattern.startsWith("*")) {
            String suffix = pattern.substring(1);
            return address.endsWith(suffix) || address.equals(suffix)
                    || host.endsWith(suffix) || host.equals(suffix);
        }
        return address.equals(pattern) || host.equals(pattern);
    }

    public static boolean matchesAny(Iterable<String> globs, String serverAddress) {
        if (globs == null) {
            return false;
        }
        for (String glob : globs) {
            if (matches(glob, serverAddress)) {
                return true;
            }
        }
        return false;
    }

    /** Splits a comma separated list, dropping blanks. */
    public static List<String> splitList(String value) {
        List<String> values = new ArrayList<>();
        if (value == null) {
            return values;
        }
        for (String part : value.split(",")) {
            String trimmed = part.trim();
            if (!trimmed.isEmpty()) {
                values.add(trimmed);
            }
        }
        return values;
    }

    /** Strips a trailing dot, the port and any trailing path from an address. */
    public static String normalize(String value) {
        String normalized = value.trim().toLowerCase(Locale.ROOT);
        if (normalized.endsWith(".")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    public static String hostOf(String value) {
        String normalized = normalize(value);
        int slash = normalized.indexOf('/');
        if (slash >= 0) {
            normalized = normalized.substring(0, slash);
        }
        int colon = normalized.lastIndexOf(':');
        if (colon > 0 && normalized.indexOf(':') == colon && isPort(normalized.substring(colon + 1))) {
            normalized = normalized.substring(0, colon);
        }
        return normalized;
    }

    private static boolean isPort(String value) {
        if (value.isEmpty() || value.length() > 5) {
            return false;
        }
        for (int i = 0; i < value.length(); i++) {
            if (!Character.isDigit(value.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
