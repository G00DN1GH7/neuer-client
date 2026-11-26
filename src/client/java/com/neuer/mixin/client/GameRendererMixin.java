package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.impl.RenderOptimizer;
import com.neuer.client.setting.Setting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private int itemActivationTicks;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (RenderOptimizer.INSTANCE != null && RenderOptimizer.INSTANCE.isToggled()) {
            Setting fastTotem = NeuerClient.INSTANCE.settingsManager.getSettingByName(RenderOptimizer.INSTANCE, "Fast Totem Anim");
            if (fastTotem != null && fastTotem.getValBoolean()) {
                if (this.itemActivationTicks > 0) {
                    this.itemActivationTicks -= 2;
                    if (this.itemActivationTicks < 0) this.itemActivationTicks = 0;
                }
            }
        }
    }

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void onBobHurt(net.minecraft.client.renderer.state.level.CameraRenderState renderState, PoseStack poseStack, CallbackInfo ci) {
        if (RenderOptimizer.INSTANCE != null && RenderOptimizer.INSTANCE.isToggled()) {
            Setting noHurtCam = NeuerClient.INSTANCE.settingsManager.getSettingByName(RenderOptimizer.INSTANCE, "No Hurt Cam");
            if (noHurtCam != null && noHurtCam.getValBoolean()) {
                ci.cancel();
            }
        }
    }

}
