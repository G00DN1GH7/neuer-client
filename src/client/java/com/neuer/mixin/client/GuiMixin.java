package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.glassui.bridge.ClientNotificationService;
import com.neuer.client.module.Module;
import com.neuer.client.module.ModuleManager;
import com.neuer.client.module.impl.RenderOptimizer;
import com.neuer.client.setting.Setting;
import com.neuer.client.setting.SettingsManager;
import com.neuer.client.ui.ArrayListHud;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class GuiMixin {

    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void onDisplayScoreboardSidebar(GuiGraphicsExtractor guiGraphics, Objective objective, CallbackInfo ci) {
        RenderOptimizer ro = RenderOptimizer.INSTANCE;
        NeuerClient client = NeuerClient.INSTANCE;
        if (ro != null && ro.isToggled() && client != null && client.settingsManager != null) {
            SettingsManager sm = client.settingsManager;
            Setting noScoreboard = sm.getSettingByName(ro, "No Scoreboard");
            if (noScoreboard != null && noScoreboard.getValBoolean()) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void onExtractRenderState(GuiGraphicsExtractor guiGraphics, DeltaTracker tickDelta, CallbackInfo ci) {
        NeuerClient client = NeuerClient.INSTANCE;
        if (client != null && client.moduleManager != null) {
            ModuleManager mm = client.moduleManager;
            for (Module m : mm.modules) {
                if (m.isToggled()) {
                    m.onRenderHUD(guiGraphics, tickDelta);
                }
            }
        }
        ArrayListHud.INSTANCE.render(guiGraphics, tickDelta.getGameTimeDeltaTicks());
        ClientNotificationService.renderHud(guiGraphics);
    }
}
