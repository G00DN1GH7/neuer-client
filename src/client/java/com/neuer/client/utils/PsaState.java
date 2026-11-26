package com.neuer.client.utils;

import net.minecraft.client.Minecraft;

import java.io.File;

public final class PsaState {
    private static volatile Boolean acceptedCache = null;

    private PsaState() {}

    public static boolean isAccepted(Minecraft mc) {
        if (acceptedCache == null) {
            File psaFile = new File(mc.gameDirectory, "config/neuerclient/psa_accepted");
            acceptedCache = psaFile.exists();
        }
        return acceptedCache;
    }

    public static void markAccepted() {
        acceptedCache = true;
    }
}
