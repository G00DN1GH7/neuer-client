package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import com.neuer.client.setting.Setting;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public class NoSlowMixin {

    /**
     * Returns the speed multiplier of the item being used (0.2 for a sword, 1.0 for food).
     *
     * <p>Target lives in {@code LocalPlayer} as {@code private float itemUseSpeedMultiplier()} in
     * Minecraft 26.3, which ships unobfuscated, so no refmap is involved and the names in this
     * file are the runtime names. {@code require = 0} keeps a future rename from aborting startup;
     * the module then simply does nothing instead of crashing the game.</p>
     */
    @Inject(method = "itemUseSpeedMultiplier", at = @At("RETURN"), cancellable = true, require = 0)
    private void neuer$applyNoSlow(CallbackInfoReturnable<Float> cir) {
        NeuerClient client = NeuerClient.INSTANCE;
        if (client == null || client.moduleManager == null || client.settingsManager == null) {
            return;
        }
        Module noSlow = client.moduleManager.getModule("NoSlow");
        if (noSlow == null || !noSlow.isToggled()) {
            return;
        }
        Setting multiplier = client.settingsManager.getSettingByName(noSlow, "Multiplier");
        if (multiplier == null) {
            return;
        }
        float configured = (float) multiplier.getValDouble();
        if (configured > cir.getReturnValueF()) {
            cir.setReturnValue(configured);
        }
    }
}
