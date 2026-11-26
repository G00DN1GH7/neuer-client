package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;

public class NoSlow extends Module {
    public NoSlow() {
        super("NoSlow", Category.Movement, "Reduces the movement slowdown while using items.");
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Multiplier", this, 0.6, 0.2, 1.0, false));
    }
}
