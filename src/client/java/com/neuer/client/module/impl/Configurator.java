package com.neuer.client.module.impl;

import com.neuer.client.glassui.bridge.ClientGuiScreen;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class Configurator extends Module {
    public Configurator() {
        super("Config Menu", Category.Configs, "Opens the config center to save, load, export and import configs.");
    }

    @Override
    public void onEnable() {
        if (mc != null && mc.level != null && mc.player != null) {
            ClientGuiScreen.openConfigCenter();
        }
        this.setToggled(false);
    }
}
