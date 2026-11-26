package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;

public class Bypass extends Module {
    public Bypass() {
        super("Bypass", Category.Client, "Hides visuals and makes GUI require looking down + 5s hold.");
        NeuerClient.INSTANCE.settingsManager.rSetting(new Setting("Disable Command Autofill", this, false));
    }
}
