package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class FastThrowables extends Module {
    private Setting speed;

    public FastThrowables() {
        super("FastThrowables", Category.Combat, "Throws experience bottles, eggs, and snowballs very fast.");
        speed = new Setting("Speed", this, 5.0, 1.0, 20.0, true);
        NeuerClient.INSTANCE.settingsManager.rSetting(speed);
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.gameMode == null)
            return;

        if (!mc.options.keyUse.isDown())
            return;

        ItemStack stack = mc.player.getMainHandItem();
        if (isThrowable(stack.getItem())) {
            int throwCount = (int) speed.getValDouble();
            for (int i = 0; i < throwCount; i++) {
                mc.gameMode.useItem(mc.player, InteractionHand.MAIN_HAND);
            }
        }
    }

    private boolean isThrowable(Item item) {
        return item == Items.EXPERIENCE_BOTTLE || item == Items.EGG || item == Items.SNOWBALL;
    }
}
