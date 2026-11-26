package com.neuer.client.render;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.impl.ESP;
import xyz.breadloaf.imguimc.interfaces.Renderable;
import xyz.breadloaf.imguimc.interfaces.Theme;
import xyz.breadloaf.imguimc.theme.ImGuiDarkTheme;

public class ESPRenderable implements Renderable {
    private static final Theme THEME = new ImGuiDarkTheme();

    @Override
    public String getName() {
        return "ESP Overlay";
    }

    @Override
    public Theme getTheme() {
        return THEME;
    }

    @Override
    public void render() {
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return;
        ESP esp = (ESP) NeuerClient.INSTANCE.moduleManager.getModule("ESP");
        if (esp != null && esp.isToggled()) {
            esp.renderImGuiOverlay();
        }
    }
}
