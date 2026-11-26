package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import com.neuer.client.module.impl.RenderOptimizer;
import com.neuer.client.setting.Setting;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    @Inject(method = "submitFire", at = @At("HEAD"), cancellable = true)
    private static void onRenderFire(PoseStack poseStack, SubmitNodeCollector buffer, TextureAtlasSprite textureAtlasSprite, CallbackInfo ci) {
        if (NeuerClient.INSTANCE != null && NeuerClient.INSTANCE.moduleManager != null) {
            Module lowFireModule = NeuerClient.INSTANCE.moduleManager.getModule("LowFire");
            if (lowFireModule != null && lowFireModule.isToggled()) {
                ci.cancel();
                return;
            }
        }

        if (RenderOptimizer.INSTANCE != null && RenderOptimizer.INSTANCE.isToggled()) {
            Setting lowFire = NeuerClient.INSTANCE.settingsManager.getSettingByName(RenderOptimizer.INSTANCE, "Low Fire");
            if (lowFire != null && lowFire.getValBoolean()) {
                poseStack.translate(0.0f, -0.3f, 0.0f);
            }
        }
    }
}
