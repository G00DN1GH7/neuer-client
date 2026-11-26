package com.neuer.client.glassui.theme;

public enum BorderStyle {
    SUBTLE,
    ACCENT,
    CRISP,
    MINIMAL;

    public static BorderStyle fromIndex(int index) {
        BorderStyle[] vals = values();
        if (index < 0 || index >= vals.length) {
            return SUBTLE;
        }
        return vals[index];
    }
}
