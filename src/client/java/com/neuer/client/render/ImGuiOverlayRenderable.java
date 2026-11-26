package com.neuer.client.render;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.ModuleManager;
import com.neuer.client.module.impl.BlockESP;
import com.neuer.client.module.impl.ESP;
import com.neuer.client.module.impl.Nametags;
import com.neuer.client.module.impl.SpawnerFinder;
import com.neuer.client.module.impl.StorageESP;
import com.neuer.client.module.impl.SuspiciousChunks;
import com.neuer.client.module.impl.TargetHUD;
import com.neuer.client.module.impl.Tracers;
import com.neuer.client.module.impl.Trajectories;
import com.neuer.client.module.impl.WeakDevice;
import com.neuer.client.ui.ArrayListHud;
import xyz.breadloaf.imguimc.interfaces.Renderable;
import xyz.breadloaf.imguimc.interfaces.Theme;
import xyz.breadloaf.imguimc.theme.ImGuiDarkTheme;

public class ImGuiOverlayRenderable implements Renderable {
    private static final Theme THEME = new ImGuiDarkTheme();

    @Override
    public String getName() {
        return "ImGui Overlay Engine";
    }

    @Override
    public Theme getTheme() {
        return THEME;
    }

    @Override
    public void render() {
        NeuerClient client = NeuerClient.INSTANCE;
        if (client == null || client.moduleManager == null) {
            return;
        }
        ModuleManager mm = client.moduleManager;

        ESP esp = (ESP) mm.getModule("ESP");
        if (esp != null && esp.isToggled()) {
            esp.renderImGuiOverlay();
        }

        Nametags nametags = (Nametags) mm.getModule("Nametags");
        if (nametags != null && nametags.isToggled()) {
            nametags.renderImGuiOverlay();
        }

        StorageESP storageESP = (StorageESP) mm.getModule("StorageESP");
        if (storageESP != null && storageESP.isToggled()) {
            storageESP.renderImGuiOverlay();
        }

        Tracers tracers = (Tracers) mm.getModule("Tracers");
        if (tracers != null && tracers.isToggled()) {
            tracers.renderImGuiOverlay();
        }

        Trajectories trajectories = (Trajectories) mm.getModule("Trajectories");
        if (trajectories != null && trajectories.isToggled()) {
            trajectories.renderImGuiOverlay();
        }

        ArrayListHud.INSTANCE.renderImGui();

        TargetHUD targetHUD = (TargetHUD) mm.getModule("TargetHUD");
        if (targetHUD != null) {
            targetHUD.renderImGuiOverlay();
        }

        BlockESP blockESP = (BlockESP) mm.getModule("BlockESP");
        if (blockESP != null) {
            blockESP.renderImGuiOverlay();
        }

        SpawnerFinder spawnerFinder = (SpawnerFinder) mm.getModule("SpawnerFinder");
        if (spawnerFinder != null) {
            spawnerFinder.renderImGuiOverlay();
        }

        SuspiciousChunks suspiciousChunks = (SuspiciousChunks) mm.getModule("SuspiciousChunks");
        if (suspiciousChunks != null) {
            suspiciousChunks.renderImGuiOverlay();
        }

        WeakDevice weakDevice = (WeakDevice) mm.getModule("WeakDevice");
        if (weakDevice != null) {
            weakDevice.renderImGuiOverlay();
        }
    }
}
