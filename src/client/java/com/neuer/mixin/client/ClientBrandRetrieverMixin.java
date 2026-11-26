package com.neuer.mixin.client;

import net.minecraft.client.ClientBrandRetriever;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {

    @Inject(method = "getClientModName", at = @At("HEAD"), cancellable = true)
    private static void onGetClientModName(CallbackInfoReturnable<String> cir) {
        com.neuer.client.module.Module mod = com.neuer.client.NeuerClient.INSTANCE.moduleManager.getModule("ClientSpoof");
        if (mod != null && mod.isToggled() && mod instanceof com.neuer.client.module.impl.ClientSpoof cs) {
            cir.setReturnValue(cs.getSpoofedBrand());
        } else {
            cir.setReturnValue("vanilla");
        }
    }
}
