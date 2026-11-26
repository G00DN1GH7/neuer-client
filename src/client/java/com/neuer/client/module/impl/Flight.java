package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;

public class Flight extends Module {
    public Flight() {
        super("Flight", Category.Blatant, "Allows you to fly.");
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Speed", this, 1.0, 0.1, 5.0, false));
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        Setting speedSetting = NeuerClient.INSTANCE.settingsManager.getSettingByName(this, "Speed");
        float speed = speedSetting != null ? (float) speedSetting.getValDouble() : 1.0f;

        mc.player.getAbilities().flying = true;
        mc.player.getAbilities().setFlyingSpeed(speed * 0.05f);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.getAbilities().flying = false;
        }
    }
}
