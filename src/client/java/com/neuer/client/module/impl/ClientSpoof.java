package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;

public class ClientSpoof extends Module {
    public ClientSpoof() {
        super("ClientSpoof", Category.Misc, "Spoofs the client brand name sent to the server.");
        setToggled(true);
    }

    public String getSpoofedBrand() {
        Setting modeSetting = NeuerClient.INSTANCE.settingsManager.getSettingByName(this, "Mode");
        if (modeSetting == null) return "lunarclient:v2.12.0-2629";
        
        String mode = modeSetting.getValString();
        if (mode.equalsIgnoreCase("Vanilla")) {
            return "vanilla";
        } else if (mode.equalsIgnoreCase("Fabric")) {
            return "fabric";
        } else if (mode.equalsIgnoreCase("Custom")) {
            Setting customSetting = NeuerClient.INSTANCE.settingsManager.getSettingByName(this, "Custom Brand");
            return customSetting != null ? customSetting.getValText() : "vanilla";
        }
        return "lunarclient:v2.12.0-2629";
    }
}
