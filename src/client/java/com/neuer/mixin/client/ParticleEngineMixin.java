package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {

    @Inject(method = "add", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(Particle particle, CallbackInfo ci) {
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return;

        Module noParticles = NeuerClient.INSTANCE.moduleManager.getModule("NoParticles");
        if (noParticles != null && noParticles.isToggled()) {
            ci.cancel();
        }
    }
}

