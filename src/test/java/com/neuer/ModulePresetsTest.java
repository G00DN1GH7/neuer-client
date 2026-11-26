package com.neuer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.neuer.client.NeuerClient;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.module.preset.ModulePresets;
import com.neuer.client.module.preset.PresetMode;
import com.neuer.client.setting.Setting;
import com.neuer.client.setting.SettingsManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ModulePresetsTest {

    @Test
    void bundledTableCoversEveryModuleWithBothModes() {
        JsonObject modules = ModulePresets.modules();
        assertFalse(modules.isEmpty(), "presets.json must contain modules");
        assertTrue(modules.size() >= 100, "expected a preset for every module, got " + modules.size());
        for (Map.Entry<String, JsonElement> entry : modules.entrySet()) {
            JsonObject modes = entry.getValue().getAsJsonObject();
            assertNotNull(modes.get("legit"), entry.getKey() + " is missing the legit preset");
            assertNotNull(modes.get("blatant"), entry.getKey() + " is missing the blatant preset");
            assertTrue(modes.get("legit").isJsonObject(), entry.getKey() + ".legit must be an object");
            assertTrue(modes.get("blatant").isJsonObject(), entry.getKey() + ".blatant must be an object");
        }
    }

    @Test
    void legitAndBlatantDifferWhereItMatters() {
        JsonObject killAura = ModulePresets.modules().getAsJsonObject("KillAura");
        double legitRange = killAura.getAsJsonObject("legit").get("Range").getAsDouble();
        double blatantRange = killAura.getAsJsonObject("blatant").get("Range").getAsDouble();
        assertTrue(legitRange < 3.5, "legit killaura range should be near vanilla, was " + legitRange);
        assertTrue(blatantRange > legitRange, "blatant killaura range should exceed legit");

        JsonObject reach = ModulePresets.modules().getAsJsonObject("Reach");
        assertTrue(
            reach.getAsJsonObject("legit").get("Distance").getAsDouble()
                < reach.getAsJsonObject("blatant").get("Distance").getAsDouble(),
            "blatant reach must be stronger than legit"
        );

        JsonObject autoClicker = ModulePresets.modules().getAsJsonObject("AutoClicker");
        assertTrue(
            autoClicker.getAsJsonObject("legit").get("Max CPS").getAsDouble()
                < autoClicker.getAsJsonObject("blatant").get("Max CPS").getAsDouble(),
            "blatant autoclicker must be faster than legit"
        );
    }

    @Test
    void appliesValuesAndClampsToSettingBounds() {
        NeuerClient previous = NeuerClient.INSTANCE;
        NeuerClient client = new NeuerClient();
        client.settingsManager = new SettingsManager();
        NeuerClient.INSTANCE = client;
        try {
            Module aura = new Module("KillAura", Category.Combat);
            Setting range = new Setting("Range", aura, 4.0, 1.0, 6.0, false);
            Setting silent = new Setting("Silent", aura, true);
            Setting rotation = new Setting("Rotation Mode", aura, "Smooth",
                new ArrayList<>(List.of("Smooth", "Bypass")));
            client.settingsManager.rSetting(range);
            client.settingsManager.rSetting(silent);
            client.settingsManager.rSetting(rotation);

            // Rotation is already "Smooth", so only Range and Silent change here.
            assertEquals(2, ModulePresets.apply(aura, PresetMode.LEGIT));
            assertEquals(3.1, range.getValDouble(), 1e-9);
            assertFalse(silent.getValBoolean());
            assertEquals("Smooth", rotation.getValString());

            assertEquals(3, ModulePresets.apply(aura, PresetMode.BLATANT));
            assertEquals(4.9, range.getValDouble(), 1e-9);
            assertTrue(silent.getValBoolean());
            assertEquals("Bypass", rotation.getValString());

            // Re-applying the same preset is a no-op, so the UI can report it honestly.
            assertEquals(0, ModulePresets.apply(aura, PresetMode.BLATANT));
        } finally {
            NeuerClient.INSTANCE = previous;
        }
    }

    @Test
    void ignoresUnknownKeysAndOutOfRangeCombos() {
        NeuerClient previous = NeuerClient.INSTANCE;
        NeuerClient client = new NeuerClient();
        client.settingsManager = new SettingsManager();
        NeuerClient.INSTANCE = client;
        try {
            Module autoClicker = new Module("AutoClicker", Category.Combat);
            Setting button = new Setting("Button", autoClicker, "Left",
                new ArrayList<>(List.of("Left", "Right")));
            Setting minCps = new Setting("Min CPS", autoClicker, 9.0, 1.0, 20.0, true);
            client.settingsManager.rSetting(button);
            client.settingsManager.rSetting(minCps);

            // The preset only touches numeric values here, so the combo stays as the user set it.
            int changed = ModulePresets.apply(autoClicker, PresetMode.LEGIT);
            assertEquals(1, changed);
            assertEquals(8.0, minCps.getValDouble(), 1e-9);
            assertEquals("Left", button.getValString());
        } finally {
            NeuerClient.INSTANCE = previous;
        }
    }

    @Test
    void everyModuleWithSettingsHasTunablesInTheTable() {
        JsonObject modules = ModulePresets.modules();
        // Spot-check the modules that were previously unregistered and easy to miss.
        for (String name : List.of("AutoArmor", "AutoTool", "BowAimbot", "FastBreak", "NoHitDelay", "NoSlow", "TrueSight")) {
            assertTrue(modules.has(name), name + " must ship a preset entry");
        }
        assertTrue(modules.getAsJsonObject("NoSlow").getAsJsonObject("legit").size() > 0,
            "NoSlow should expose a tunable multiplier in its preset");
    }
}
