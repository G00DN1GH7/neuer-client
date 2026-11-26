package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractSignEditScreen.class)
public class AutoSignScreenMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void onInit(CallbackInfo ci) {
        if (NeuerClient.INSTANCE != null && NeuerClient.INSTANCE.moduleManager != null) {
            Module autoSign = NeuerClient.INSTANCE.moduleManager.getModule("AutoSign");
            if (autoSign != null && autoSign.isToggled()) {
                com.neuer.client.module.impl.AutoSign.handleScreen((AbstractSignEditScreen)(Object)this);
            }
        }
    }
}