package com.neuer.client.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * One line of the local detection database: {@code <module>|<server>}.
 *
 * <p>{@code <module>} is a module name or {@code *}; {@code <server>} is an address or
 * {@code *}-suffix glob. Blank lines and lines starting with {@code #} are ignored.</p>
 */
public final class DetectionRule {
    private static final char SEPARATOR = '|';

    private final String modulePattern;
    private final String serverGlob;

    private DetectionRule(String modulePattern, String serverGlob) {
        this.modulePattern = modulePattern;
        this.serverGlob = serverGlob;
    }

    public static List<DetectionRule> parseLines(Iterable<String> lines) {
        List<DetectionRule> rules = new ArrayList<>();
        if (lines == null) {
            return rules;
        }
        for (String rawLine : lines) {
            if (rawLine == null) {
                continue;
            }
            String line = rawLine.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            int separator = line.indexOf(SEPARATOR);
            if (separator <= 0 || separator == line.length() - 1) {
                continue;
            }
            String module = line.substring(0, separator).trim().toLowerCase(Locale.ROOT);
            String server = line.substring(separator + 1).trim();
            if (module.isEmpty() || server.isEmpty()) {
                continue;
            }
            rules.add(new DetectionRule(module, server));
        }
        return rules;
    }

    public static List<DetectionRule> parse(String text) {
        return text == null ? new ArrayList<>() : parseLines(text.lines().toList());
    }

    public boolean matches(String moduleName, String serverAddress) {
        if (moduleName == null || serverAddress == null) {
            return false;
        }
        if (!"*".equals(modulePattern) && !modulePattern.equals(moduleName.trim().toLowerCase(Locale.ROOT))) {
            return false;
        }
        return ServerGlob.matches(serverGlob, serverAddress);
    }

    public String modulePattern() {
        return modulePattern;
    }

    public String serverGlob() {
        return serverGlob;
    }
}
