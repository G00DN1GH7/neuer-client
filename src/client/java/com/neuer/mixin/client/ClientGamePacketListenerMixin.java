package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.Module;
import com.neuer.client.module.impl.AutoTotem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientGamePacketListenerMixin {

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void onHandleEntityEventHead(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (packet.getEventId() == 30 && NeuerClient.INSTANCE != null) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level != null) {
                Entity eventEntity = packet.getEntity(mc.level);
                if (eventEntity != null) {
                    com.neuer.client.module.impl.WebStun.onShieldBreak(eventEntity);
                }
            }
        }
    }

    @Inject(method = "handleEntityEvent", at = @At("TAIL"))
    private void onHandleEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        if (packet.getEventId() != 35) return;
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) return;

        Entity eventEntity = packet.getEntity(mc.level);
        if (eventEntity != mc.player) return;

        Module autoTotemModule = NeuerClient.INSTANCE.moduleManager.getModule("AutoTotem");
        if (autoTotemModule instanceof AutoTotem autoTotem && autoTotemModule.isToggled()) {
            autoTotem.onLocalTotemPop();
        }
    }

    @Inject(method = "handleCommands", at = @At("HEAD"), cancellable = true)
    private void onHandleCommands(net.minecraft.network.protocol.game.ClientboundCommandsPacket packet, CallbackInfo ci) {
        if (NeuerClient.INSTANCE != null && NeuerClient.INSTANCE.moduleManager != null) {
            Module bypass = NeuerClient.INSTANCE.moduleManager.getModule("Bypass");
            if (bypass != null && bypass.isToggled()) {
                com.neuer.client.setting.Setting disableAutofill = NeuerClient.INSTANCE.settingsManager.getSettingByName(bypass, "Disable Command Autofill");
                if (disableAutofill != null && disableAutofill.getValBoolean()) {
                    ci.cancel();
                }
            }
        }
    }

    @Inject(method = "handleExplosion", at = @At("HEAD"))
    private void onHandleExplosion(net.minecraft.network.protocol.game.ClientboundExplodePacket packet, CallbackInfo ci) {
        com.neuer.client.module.impl.JumpReset.lastExplosionTime = System.currentTimeMillis();
    }

    @Inject(method = "handleSetEntityMotion", at = @At("TAIL"))
    private void onHandleSetEntityMotion(net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket packet, CallbackInfo ci) {
        if (System.currentTimeMillis() - com.neuer.client.module.impl.JumpReset.lastExplosionTime < 1000L) return;
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return;
        com.neuer.client.module.impl.JumpReset jumpReset = (com.neuer.client.module.impl.JumpReset) NeuerClient.INSTANCE.moduleManager.getModule("JumpReset");
        if (jumpReset == null || !jumpReset.isToggled()) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (packet.id() == mc.player.getId()) {
            net.minecraft.world.phys.Vec3 velocity = packet.movement();
            double velocityMagnitude = Math.sqrt(velocity.x * velocity.x + velocity.y * velocity.y + velocity.z * velocity.z);
            com.neuer.client.setting.Setting thresholdSetting = NeuerClient.INSTANCE.settingsManager.getSettingByName(jumpReset, "Velocity Threshold");
            double threshold = thresholdSetting != null ? thresholdSetting.getValDouble() : 0.1;
            if (velocityMagnitude > threshold) {
                jumpReset.onKnockback();
            }
        }
    }

    @Inject(method = "handleRespawn", at = @At("HEAD"))
    private void onHandleRespawn(net.minecraft.network.protocol.game.ClientboundRespawnPacket packet, CallbackInfo ci) {
        com.neuer.client.utils.ModuleUtils.resetServerSlot();
    }
}

