package com.neuer.client.gui;

import com.neuer.client.glassui.bridge.ClientGuiScreen;
import net.minecraft.client.gui.screens.Screen;

public class MenuConfigScreen extends ClientGuiScreen {
    private final Screen parent;

    public MenuConfigScreen(Screen parent) {
        super(parent);
        this.parent = parent;
    }

    public Screen parent() {
        return parent;
    }
}
