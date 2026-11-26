package com.neuer.client.glassui.theme;

public enum FontGradientStyle {
    PEARL,
    AURORA,
    CHAMPAGNE,
    PLATINUM;

    public static FontGradientStyle fromIndex(int index) {
        FontGradientStyle[] vals = values();
        if (index < 0 || index >= vals.length) {
            return PEARL;
        }
        return vals[index];
    }
}
