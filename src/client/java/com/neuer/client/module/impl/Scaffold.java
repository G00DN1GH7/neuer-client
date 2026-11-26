package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class Scaffold extends Module {

    public Scaffold() {
        super("Scaffold", Category.World, "Macro: Holds S, enables BridgeAssist & FastPlace.");
    }

    @Override
    public void onEnable() {
        Module bridgeAssist = NeuerClient.INSTANCE.moduleManager.getModule("BridgeAssist");
        Module fastPlace = NeuerClient.INSTANCE.moduleManager.getModule("FastPlace");
        
        if (bridgeAssist != null && !bridgeAssist.isToggled()) bridgeAssist.toggle();
        if (fastPlace != null && !fastPlace.isToggled()) fastPlace.toggle();
    }

    @Override
    public void onTick() {
        if (mc != null && mc.player != null && mc.gui.screen() == null) {
            mc.options.keyDown.setDown(true);
            mc.options.keyUse.setDown(true);
        }
    }

    @Override
    public void onDisable() {
        Module bridgeAssist = NeuerClient.INSTANCE.moduleManager.getModule("BridgeAssist");
        Module fastPlace = NeuerClient.INSTANCE.moduleManager.getModule("FastPlace");
        
        if (bridgeAssist != null && bridgeAssist.isToggled()) bridgeAssist.toggle();
        if (fastPlace != null && fastPlace.isToggled()) fastPlace.toggle();

        if (mc.options != null) {
            mc.options.keyDown.setDown(false);
            mc.options.keyUse.setDown(false);
        }
    }
}