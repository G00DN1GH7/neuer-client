package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class LocalPlayerMixin {

    @Inject(method = "displayItemActivation", at = @At("HEAD"), cancellable = true)
    private void onDisplayItemActivation(ItemStack stack, CallbackInfo ci) {
        if (stack == null || !stack.is(Items.TOTEM_OF_UNDYING)) return;
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return;

        Module noTotemPop = NeuerClient.INSTANCE.moduleManager.getModule("NoTotemPop");
        if (noTotemPop != null && noTotemPop.isToggled()) {
            ci.cancel();
        }
    }
}
