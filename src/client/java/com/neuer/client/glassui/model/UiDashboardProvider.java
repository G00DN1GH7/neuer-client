package com.neuer.client.glassui.model;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public interface UiDashboardProvider {
    default Identifier avatarSkin(Minecraft mc) {
        return null;
    }

    default boolean hasActiveStatus() {
        return false;
    }

    UiDashboardSnapshot buildSnapshot(Minecraft mc);
}
