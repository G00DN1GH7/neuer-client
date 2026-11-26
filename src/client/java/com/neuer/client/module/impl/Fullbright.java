package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class Fullbright extends Module {
    

    public Fullbright() {
        super("Fullbright", Category.Render, "Lights up your world using Gamma configuration!");
    }

    @Override
    public void onTick() {
        if (mc.player == null) return;

        com.neuer.client.setting.Setting mode = 
            com.neuer.client.NeuerClient.INSTANCE.settingsManager.getSettingByName(this, "Mode");

        if (mode != null && mode.getValString().equals("Gamma")) {
            if (mc.player != null) {
                mc.player.removeEffect(MobEffects.NIGHT_VISION);
            }
        } else if (mode != null && mode.getValString().equals("Night Vision")) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 999999, 0, false, false, false));
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.removeEffect(MobEffects.NIGHT_VISION);
        }
    }
}