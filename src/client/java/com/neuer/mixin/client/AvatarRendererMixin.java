package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import com.neuer.client.utils.RotationManager;
import net.minecraft.world.entity.Avatar;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public class AvatarRendererMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Avatar;Lnet/minecraft/client/renderer/entity/state/AvatarRenderState;F)V", at = @At("TAIL"))
    private void onExtractRenderState(Avatar entity, AvatarRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity instanceof LocalPlayer && RotationManager.isActive()) {
            state.xRot = RotationManager.getServerPitch();
            if (state.isUpsideDown) {
                state.xRot *= -1.0f;
            }
        }
        if (NeuerClient.INSTANCE != null && NeuerClient.INSTANCE.moduleManager != null) {
            Module nametags = NeuerClient.INSTANCE.moduleManager.getModule("Nametags");
            if (nametags != null && nametags.isToggled()) {
                state.nameTag = null;
            }
        }
    }
}



