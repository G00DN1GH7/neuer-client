package com.neuer.client.utils;

import com.neuer.client.NeuerClient;
import com.neuer.client.glassui.bridge.ClientNotificationService;
import com.neuer.client.glassui.render.StreamproofOverlay;
import com.neuer.client.module.Module;
import com.neuer.client.setting.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

/**
 * Panic and self destruct actions.
 *
 * <p>Panic saves the (now disabled) config, closes any open screen and the streamproof surface.
 * Self destruct runs panic and then wipes every file the client owns; the second click within the
 * confirmation window (or holding the bound key) is required so it cannot fire on a stray press.
 * The mod jar is left alone until the JVM shuts down so the running client keeps its resources.</p>
 */
public final class PanicHandler {
    private static final long CONFIRM_WINDOW_MS = 5_000L;

    private static long selfDestructArmedAt = 0L;

    private PanicHandler() {
    }

    /** Disables every module, saves the config and closes the GUI. Returns how many were disabled. */
    public static int panic() {
        NeuerClient client = NeuerClient.INSTANCE;
        Minecraft mc = Minecraft.getInstance();
        if (client == null || client.moduleManager == null) {
            return 0;
        }
        selfDestructArmedAt = 0L;

        int disabled = 0;
        ClientNotificationService.setSuppressModuleHook(true);
        try {
            for (Module module : client.moduleManager.modules) {
                if (module.isToggled()) {
                    module.toggle();
                    disabled++;
                }
            }
        } finally {
            ClientNotificationService.setSuppressModuleHook(false);
        }

        ConfigManager.save();
        if (mc != null) {
            mc.setScreenAndShow(null);
            StreamproofOverlay.hide();
        }
        feedback(mc, "§d[Neuer] §7Panic: " + disabled + " module(s) disabled.");
        return disabled;
    }

    /** First call arms self destruct, a second call inside the window wipes the client. */
    public static void requestSelfDestruct() {
        Minecraft mc = Minecraft.getInstance();
        long now = System.currentTimeMillis();
        if (selfDestructArmedAt != 0L && now - selfDestructArmedAt <= CONFIRM_WINDOW_MS) {
            selfDestruct();
            return;
        }
        selfDestructArmedAt = now;
        feedback(mc, "§d[Neuer] §cSelf destruct armed — press again within 5s to wipe the client.");
    }

    /**
     * Runs panic and removes every file the client owns, then queues the mod jar for deletion at
     * shutdown. Returns how many paths were removed immediately.
     */
    public static int selfDestruct() {
        selfDestructArmedAt = 0L;
        ClientWipe.markWiped();
        panic();
        int removed = ClientWipe.purgeAll();
        ClientWipe.scheduleModFileRemovalAtShutdown();
        feedback(Minecraft.getInstance(), "§d[Neuer] §cSelf destruct: " + removed
                + " file(s) removed. The mod jar is deleted when Minecraft closes.");
        return removed;
    }

    private static void feedback(Minecraft mc, String message) {
        if (mc != null && mc.player != null) {
            mc.player.sendSystemMessage(Component.literal(message));
        }
    }
}
