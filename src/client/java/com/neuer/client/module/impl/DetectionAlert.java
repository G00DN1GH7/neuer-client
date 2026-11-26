package com.neuer.client.module.impl;

import com.neuer.client.NeuerClient;
import com.neuer.client.clickgui.DetectionWarningScreen;
import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.client.utils.ClientPaths;
import com.neuer.client.utils.DetectionRule;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Warns you when a module you just enabled is known to be detected on the current server.
 *
 * <p>Rules come from {@code config/neuerclient/detection_db.txt} when present, otherwise from
 * the rules bundled with the client. No network access.</p>
 */
public class DetectionAlert extends Module {
    private static final String LOCAL_DB_NAME = "detection_db.txt";
    private static final String BUNDLED_DB = "/assets/neuerclient/detection_db.txt";

    private List<DetectionRule> dbRules = new ArrayList<>();
    private String lastServerIp = null;
    private boolean fetched = false;
    private List<Module> previouslyEnabled = new ArrayList<>();

    public DetectionAlert() {
        super("DetectionDB", Category.Misc, "Warns you if you are using detected modules on a server (local rules).", true);
        this.setToggled(true);
    }

    @Override
    public void onTick() {
        if (!isToggled() || mc.player == null) return;

        String currentServer = mc.getCurrentServer() != null ? mc.getCurrentServer().ip : null;

        if (currentServer != null && !currentServer.equals(lastServerIp)) {
            lastServerIp = currentServer;
            previouslyEnabled.clear();

            if (!fetched) {
                loadDatabase(currentServer);
            } else {
                checkCurrentServer(currentServer);
            }
        } else if (currentServer == null) {
            lastServerIp = null;
            previouslyEnabled.clear();
        }

        if (fetched && currentServer != null && mc.gui.screen() == null) {
            List<Module> currentlyEnabled = new ArrayList<>();
            for (Module m : NeuerClient.INSTANCE.moduleManager.modules) {
                if (m.isToggled()) currentlyEnabled.add(m);
            }
            List<Module> newlyEnabled = new ArrayList<>(currentlyEnabled);
            newlyEnabled.removeAll(previouslyEnabled);

            List<Module> newlyFlagged = new ArrayList<>();
            for (Module m : newlyEnabled) {
                if (m != this && isModuleDetected(m, currentServer)) {
                    newlyFlagged.add(m);
                }
            }
            if (!newlyFlagged.isEmpty()) {
                mc.setScreenAndShow(new DetectionWarningScreen(newlyFlagged));
            }
            previouslyEnabled = currentlyEnabled;
        }
    }

    private void loadDatabase(String ip) {
        dbRules = DetectionRule.parseLines(readRuleLines());
        fetched = true;
        checkCurrentServer(ip);
    }

    private List<String> readRuleLines() {
        Path local = ClientPaths.resolve(LOCAL_DB_NAME);
        if (Files.isRegularFile(local)) {
            try {
                return Files.readAllLines(local, StandardCharsets.UTF_8);
            } catch (IOException e) {
                System.err.println("[Neuer] Failed to read " + local + ": " + e.getMessage());
            }
        }
        try (InputStream stream = DetectionAlert.class.getResourceAsStream(BUNDLED_DB)) {
            if (stream == null) {
                return List.of();
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8).lines().toList();
        } catch (IOException e) {
            System.err.println("[Neuer] Failed to read bundled detection rules: " + e.getMessage());
            return List.of();
        }
    }

    private void checkCurrentServer(String ip) {
        if (ip == null || dbRules.isEmpty() || mc.player == null) return;

        List<Module> flagged = new ArrayList<>();
        for (Module m : NeuerClient.INSTANCE.moduleManager.modules) {
            if (m.isToggled() && m != this && isModuleDetected(m, ip)) {
                flagged.add(m);
            }
        }

        if (!flagged.isEmpty()) {
            mc.setScreenAndShow(new DetectionWarningScreen(flagged));
        }
    }

    public boolean isModuleDetected(Module m, String ip) {
        if (ip == null || dbRules.isEmpty()) return false;
        for (DetectionRule rule : dbRules) {
            if (rule.matches(m.getName(), ip)) {
                return true;
            }
        }
        return false;
    }
}
