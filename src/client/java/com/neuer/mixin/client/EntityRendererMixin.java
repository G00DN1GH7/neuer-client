package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.impl.Chams;
import com.neuer.client.utils.RotationManager;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity, S extends EntityRenderState> {

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V", at = @At("TAIL"))
    private void onExtractRenderState(T entity, S state, float partialTicks, CallbackInfo ci) {
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return;
        Chams chams = (Chams) NeuerClient.INSTANCE.moduleManager.getModule("Chams");
        if (chams != null && chams.isToggled() && chams.shouldGlow(entity)) {
            state.outlineColor = chams.getGlowColor(entity);
        }
    }
}
