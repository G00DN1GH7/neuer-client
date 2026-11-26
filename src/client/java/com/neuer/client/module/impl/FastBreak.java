package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;

public class FastBreak extends Module {
    public FastBreak() {
        super("FastBreak", Category.World, "Slightly shortens the block break delay within a vanilla-safe multiplier.");
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Speed Multiplier", this, 1.07, 1.05, 1.10, false));
    }
}
