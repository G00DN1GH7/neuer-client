package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;

public class RenderOptimizer extends Module {
    public static RenderOptimizer INSTANCE;

    public RenderOptimizer() {
        super("RenderOptimizer", Category.Render, "Optimizes standard UI rendering and overlays.");
        INSTANCE = this;
        
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("No Scoreboard", this, true));
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("No Hurt Cam", this, true));
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("No Bobbing", this, true));
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Low Fire", this, true));
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Low Shield", this, true));
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Low Totem", this, true));
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Fast Totem Anim", this, true));
    }

    @Override
    public void onTick() {
        if (!isToggled() || mc.player == null) return;
        
        Setting noBob = NeuerClient.INSTANCE.settingsManager.getSettingByName(this, "No Bobbing");
        if (noBob != null && noBob.getValBoolean()) {
            if (mc.options.bobView().get()) {
                mc.options.bobView().set(false);
            }
        }
    }
}
