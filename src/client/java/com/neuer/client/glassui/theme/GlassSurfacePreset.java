package com.neuer.client.glassui.theme;

public enum GlassSurfacePreset {
    STONE,
    OBSIDIAN,
    GLACIER,
    MIDNIGHT;

    public static GlassSurfacePreset fromIndex(int index) {
        GlassSurfacePreset[] vals = values();
        if (index < 0 || index >= vals.length) {
            return STONE;
        }
        return vals[index];
    }
}
