package com.neuer.client.clickgui;

import com.neuer.client.glassui.render.SatoshiFont;
import com.neuer.client.glassui.render.SatoshiWeight;
import com.neuer.client.glassui.render.TextGradient;
import com.neuer.client.glassui.render.UiRenderPrimitives;
import com.neuer.client.glassui.theme.UiTheme;
import com.neuer.client.glassui.theme.UiThemeContext;
import com.neuer.client.utils.PsaState;
import java.io.File;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PSAScreen extends Screen {
    private final Screen parentScreen;

    public PSAScreen(Screen parentScreen) {
        super(Component.literal("PSA Warning"));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        super.init();
        int btnWidth = 180;
        int btnHeight = 20;
        int btnX = this.width / 2 - btnWidth / 2;
        int btnY = this.height / 2 + 76;

        Button.Builder continueBuilder = Button.builder(Component.literal("I Understand"), btn -> {
            try {
                File dir = new File(this.minecraft.gameDirectory, "config/neuerclient");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File psaFile = new File(dir, "psa_accepted");
                psaFile.createNewFile();
                PsaState.markAccepted();
            } catch (Exception ignored) {
            }
            if (this.minecraft != null) {
                this.minecraft.setScreenAndShow(this.parentScreen);
            }
        });
        continueBuilder.bounds(btnX, btnY, btnWidth, btnHeight);
        this.addRenderableWidget(continueBuilder.build());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.fill(0, 0, this.width, this.height, UiRenderPrimitives.argb(185, 10, 10, 14));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        UiTheme theme = UiThemeContext.current();
        int accent = theme.accentColor();
        float cx = this.width * 0.5f;
        float cy = this.height * 0.5f;
        float cardW = Math.min(this.width - 28f, 430f);
        float cardH = 206f;

        UiRenderPrimitives.drawRoundedShadow(guiGraphics, cx - cardW * 0.5f, cy - 98f, cardW, cardH, 15f, 14f, UiRenderPrimitives.argb(115, 0, 0, 0));
        UiRenderPrimitives.drawGlassCard(
            guiGraphics,
            cx - cardW * 0.5f,
            cy - 100f,
            cardW,
            cardH,
            15f,
            theme.windowTopColor(236),
            theme.windowBottomColor(244),
            UiRenderPrimitives.argb(82, 255, 255, 255),
            UiRenderPrimitives.argb(28, 255, 255, 255)
        );

        SatoshiFont.drawCentered(guiGraphics, "About Neuer Client", cx, cy - 82f, 8.2f, TextGradient.accent(1f, 0xFFFF6470, theme), SatoshiWeight.SEMIBOLD);
        SatoshiFont.drawCentered(guiGraphics, "Private build for personal use — do not redistribute.", cx, cy - 60f, 5.6f, TextGradient.pearl(1f, accent, 0.25f, theme), SatoshiWeight.SEMIBOLD);
        SatoshiFont.drawCentered(guiGraphics, "Based on Marlow Client by nxghtCry0, NPSL 1.0.", cx, cy - 42f, 5.5f, TextGradient.accent(1f, accent, theme), SatoshiWeight.SEMIBOLD);
        SatoshiFont.drawCentered(guiGraphics, "Recommended configs: config/neuerclient/configs", cx, cy - 22f, 5.2f, TextGradient.silver(0.90f, theme), SatoshiWeight.MEDIUM);
        SatoshiFont.drawCentered(guiGraphics, "Detection rules: config/neuerclient/detection_db.txt", cx, cy - 8f, 5.0f, TextGradient.mist(0.80f, theme), SatoshiWeight.MEDIUM);
        SatoshiFont.drawCentered(guiGraphics, "Thanks for using Neuer Client — original by nxght", cx, cy + 28f, 5.4f, TextGradient.pearl(0.95f, accent, 0.34f, theme), SatoshiWeight.SEMIBOLD);

        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);
    }
}
