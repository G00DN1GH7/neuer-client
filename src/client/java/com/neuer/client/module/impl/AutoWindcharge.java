package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public class AutoWindcharge extends Module {
    private boolean active = false;
    private int ticksElapsed = 0;
    private int originalSlot = -1;

    public AutoWindcharge() {
        super("AutoWindcharge", Category.Mace, "Automatically throws a windcharge at your feet.");
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Delay Ticks", this, 2.0, 1.0, 10.0, true));
    }

    private int chargeSlot = -1;

    @Override
    public void onKeybind() {
        if (mc.player == null || mc.getConnection() == null || active) return;

        int slot = findItem("wind_charge");
        if (slot == -1) {
            super.onKeybind();
            return;
        }

        originalSlot = com.neuer.client.utils.ModuleUtils.getSelectedSlot();
        chargeSlot = slot;
        active = true;
        ticksElapsed = 0;
    }

    @Override
    public void onTick() {
        if (!active || mc.player == null || mc.getConnection() == null) return;

        ticksElapsed++;

        Setting delaySetting = NeuerClient.INSTANCE.settingsManager.getSettingByName(this, "Delay Ticks");
        int delay = delaySetting != null ? (int) delaySetting.getValDouble() : 2;

        if (ticksElapsed <= delay + 3) {
            com.neuer.client.utils.SilentAimUtil.setRotation(mc.player.getYRot(), 90.0f, 2);
        }

        if (ticksElapsed == 1) {
            com.neuer.client.utils.ModuleUtils.switchToSlot(chargeSlot);
        } else if (ticksElapsed == delay + 1) {
            com.neuer.client.utils.ModuleUtils.useItemPacket(mc.player.getYRot(), 90.0f);
        } else if (ticksElapsed > delay + 3) {
            com.neuer.client.utils.ModuleUtils.switchToSlot(originalSlot);
            active = false;
        }
    }

    @Override
    public void onDisable() {
        if (active) {
            com.neuer.client.utils.ModuleUtils.switchToSlot(originalSlot);
        }
        active = false;
    }

    private int findItem(String targetName) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getItem(i);
            String itemName = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath();
            if (itemName.equals(targetName)) {
                return i;
            }
        }
        return -1;
    }
}
