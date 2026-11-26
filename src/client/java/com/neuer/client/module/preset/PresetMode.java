package com.neuer.client.module.preset;

/**
 * The two recommended configurations every module ships with.
 *
 * <p>LEGIT keeps values inside the ranges that anticheats such as Grim, Vulcan and
 * Intave accept without flags. BLATANT uses the strongest values the module supports
 * and is meant for unrestricted servers or singleplayer testing.</p>
 */
public enum PresetMode {
    LEGIT(
        "legit",
        "Legit (Bypass)",
        "Conservative, anticheat-safe values."
    ),
    BLATANT(
        "blatant",
        "Blatant (Unsafe)",
        "Maximum values — expect flags on monitored servers."
    );

    private final String key;
    private final String label;
    private final String summary;

    PresetMode(String key, String label, String summary) {
        this.key = key;
        this.label = label;
        this.summary = summary;
    }

    /** JSON key inside a module's preset object. */
    public String key() {
        return key;
    }

    /** Human readable name shown in the UI. */
    public String label() {
        return label;
    }

    /** One line description shown under the preset buttons. */
    public String summary() {
        return summary;
    }
}
