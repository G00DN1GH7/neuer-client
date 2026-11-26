package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import com.neuer.client.module.ModuleManager;
import com.neuer.client.module.impl.AutoTotem;
import com.neuer.client.module.impl.STap;
import com.neuer.client.module.impl.SprintResetController;
import com.neuer.client.module.impl.WTap;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class AutoSprintMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        NeuerClient client = NeuerClient.INSTANCE;
        if (client == null || client.moduleManager == null) {
            return;
        }
        ModuleManager mm = client.moduleManager;
        Module sprintMod = mm.getModule("AutoSprint");
        if (sprintMod == null || !sprintMod.isToggled()) {
            return;
        }

        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player.zza > 0 && !player.isShiftKeyDown() && !player.isUsingItem()) {
            if (!SprintResetController.isSuppressingSprint()
                && !WTap.shouldSilentStopSprint()
                && !STap.shouldSilentStopSprint()
                && !AutoTotem.shouldPauseInputs()) {
                player.setSprinting(true);
            }
        }
    }
}
