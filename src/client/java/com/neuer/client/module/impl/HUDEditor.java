package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class HUDEditor extends Module {
    public HUDEditor() {
        super("HUDEditor", Category.HUD, "Opens the interactive screen overlay editor.");
        this.setKeyBind(com.mojang.blaze3d.platform.InputConstants.KEY_GRAVE);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) {
            setToggled(false);
            return;
        }
        mc.setScreenAndShow(new com.neuer.client.clickgui.HudEditorScreen());
        setToggled(false);
    }
}
