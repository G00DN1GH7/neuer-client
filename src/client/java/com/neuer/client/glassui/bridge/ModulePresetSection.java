package com.neuer.client.glassui.bridge;

import com.neuer.client.glassui.asset.LucideIcon;
import com.neuer.client.glassui.model.UiCustomSection;
import com.neuer.client.glassui.model.UiHitRect;
import com.neuer.client.glassui.model.UiScreenActions;
import com.neuer.client.glassui.model.UiSectionMeasureContext;
import com.neuer.client.glassui.model.UiSectionRenderContext;
import com.neuer.client.glassui.render.SatoshiFont;
import com.neuer.client.glassui.render.SatoshiWeight;
import com.neuer.client.glassui.render.SmoothScroller;
import com.neuer.client.glassui.render.TextGradient;
import com.neuer.client.glassui.render.UiRenderPrimitives;
import com.neuer.client.glassui.theme.UiTheme;
import com.neuer.client.module.Module;
import com.neuer.client.module.preset.ModulePresets;
import com.neuer.client.module.preset.PresetMode;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * The "Recommended Presets" block shown on every module card: one click applies the
 * bypass-safe (legit) or the maximum (blatant) values from the bundled preset table.
 */
public final class ModulePresetSection implements UiCustomSection {
    private static final int BLATANT_COLOR = 0xFFFF5A5A;

    private final Module module;

    private float legitHover = 0f;
    private float blatantHover = 0f;
    private UiHitRect legitHitbox = UiHitRect.EMPTY;
    private UiHitRect blatantHitbox = UiHitRect.EMPTY;

    private String lastLabel = "";
    private long lastFlashAt = 0L;

    public ModulePresetSection(Module module) {
        this.module = module;
    }

    @Override
    public float measureHeight(UiSectionMeasureContext context) {
        return 92f;
    }

    @Override
    public void clearHitboxes() {
        legitHitbox = UiHitRect.EMPTY;
        blatantHitbox = UiHitRect.EMPTY;
    }

    @Override
    public void render(UiSectionRenderContext context) {
        GuiGraphicsExtractor graphics = context.graphics();
        SmoothScroller scroller = context.scroller();
        float areaX = context.areaX();
        float areaW = context.areaW();
        float curY = context.startY();
        float viewTop = context.viewTop();
        float viewBottom = context.viewBottom();
        float lx = context.mouseX();
        float ly = context.mouseY();
        float animDt = context.animDt();
        float alpha = context.alpha() * scroller.edgeFadeAlpha(curY, 86f, viewTop, viewBottom);
        boolean interactive = context.interactive();
        UiTheme theme = context.theme();
        int accent = theme.accentColor();
        float rScale = theme.cornerRadiusScale();

        UiRenderPrimitives.drawThemedGlassCard(
            graphics,
            areaX,
            curY,
            areaW,
            84f,
            11f * rScale,
            30f,
            17f,
            46f,
            20f,
            alpha,
            0f,
            0f,
            theme
        );

        UiRenderPrimitives.drawIcon(
            graphics,
            LucideIcon.SHIELD,
            areaX + 12f,
            curY + 11f,
            12f,
            UiRenderPrimitives.multiplyAlpha(accent, alpha)
        );
        SatoshiFont.draw(
            graphics,
            "Recommended Presets",
            areaX + 29f,
            curY + 11.2f,
            6.1f,
            TextGradient.pearl(alpha, accent, 0.28f, theme),
            SatoshiWeight.SEMIBOLD
        );

        int legitCount = ModulePresets.presetSize(module, PresetMode.LEGIT);
        int blatantCount = ModulePresets.presetSize(module, PresetMode.BLATANT);
        String subtitle = "Legit = " + legitCount + " bypass-safe values  •  Blatant = "
            + blatantCount + " maximum values. Applied in one click.";
        SatoshiFont.draw(
            graphics,
            UiRenderPrimitives.ellipsize(subtitle, areaW - 42f, 4.8f, SatoshiWeight.MEDIUM),
            areaX + 29f,
            curY + 23.5f,
            4.8f,
            TextGradient.mist(0.78f * alpha, theme),
            SatoshiWeight.MEDIUM
        );

        float gap = 8f;
        float btnW = (areaW - 24f - gap) * 0.5f;
        float btnY = curY + 36f;
        float btnH = 24f;
        float legitX = areaX + 12f;
        float blatantX = legitX + btnW + gap;

        UiHitRect legitRect = new UiHitRect(legitX, btnY, btnW, btnH);
        UiHitRect blatantRect = new UiHitRect(blatantX, btnY, btnW, btnH);
        if (interactive) {
            legitHitbox = legitRect;
            blatantHitbox = blatantRect;
        }

        legitHover = UiRenderPrimitives.approachExp(legitHover, (interactive && legitRect.contains(lx, ly)) ? 1f : 0f, 16f, animDt);
        blatantHover = UiRenderPrimitives.approachExp(blatantHover, (interactive && blatantRect.contains(lx, ly)) ? 1f : 0f, 16f, animDt);

        drawPresetButton(graphics, legitRect, "Legit (Bypass)", LucideIcon.CHECK, legitHover, accent, alpha, theme);
        drawPresetButton(graphics, blatantRect, "Blatant (Unsafe)", LucideIcon.DANGER, blatantHover, BLATANT_COLOR, alpha, theme);

        String status = lastLabel.isEmpty()
            ? "Nothing applied yet — presets only change this module's options."
            : lastLabel;
        SatoshiFont.draw(
            graphics,
            UiRenderPrimitives.ellipsize(status, areaW - 24f, 4.6f, SatoshiWeight.MEDIUM),
            areaX + 12f,
            curY + 68f,
            4.6f,
            TextGradient.silver(0.72f * alpha, theme),
            SatoshiWeight.MEDIUM
        );
    }

    private static void drawPresetButton(
        GuiGraphicsExtractor graphics,
        UiHitRect rect,
        String label,
        LucideIcon icon,
        float hover,
        int color,
        float alpha,
        UiTheme theme
    ) {
        float radius = Math.min(9.5f, 9.5f * theme.cornerRadiusScale());
        UiRenderPrimitives.drawThemedGlassCard(
            graphics,
            rect.x(),
            rect.y(),
            rect.w(),
            rect.h(),
            radius,
            32f + 22f * hover,
            18f + 14f * hover,
            52f + 30f * hover,
            22f + 14f * hover,
            alpha,
            0.42f + 0.30f * hover,
            hover,
            theme
        );

        float iconSize = 10f;
        float fontSize = 5.5f;
        float textWidth = SatoshiFont.width(label, fontSize, SatoshiWeight.SEMIBOLD);
        float totalWidth = iconSize + 6f + textWidth;
        float startX = rect.x() + (rect.w() - totalWidth) * 0.5f;
        UiRenderPrimitives.drawIcon(
            graphics,
            icon,
            startX,
            rect.y() + (rect.h() - iconSize) * 0.5f,
            iconSize,
            UiRenderPrimitives.multiplyAlpha(color, alpha)
        );
        SatoshiFont.draw(
            graphics,
            label,
            startX + iconSize + 6f,
            rect.y() + (rect.h() - fontSize) * 0.5f,
            fontSize,
            TextGradient.pearl(alpha, theme.accentColor(), 0.24f, theme),
            SatoshiWeight.SEMIBOLD
        );
    }

    @Override
    public boolean onMouseClicked(UiScreenActions actions, float localX, float localY, int button) {
        if (button != 0) {
            return false;
        }
        if (!legitHitbox.contains(localX, localY) && !blatantHitbox.contains(localX, localY)) {
            return false;
        }
        PresetMode mode = legitHitbox.contains(localX, localY) ? PresetMode.LEGIT : PresetMode.BLATANT;
        applyPreset(mode);
        return true;
    }

    private void applyPreset(PresetMode mode) {
        long now = System.currentTimeMillis();
        if (now - lastFlashAt < 150L) {
            return;
        }
        lastFlashAt = now;
        int changed = ModulePresets.apply(module, mode);
        lastLabel = changed == 0
            ? mode.label() + " — already at recommended values"
            : mode.label() + " — " + changed + " setting" + (changed == 1 ? "" : "s") + " updated";
        ClientGuiRegistry.onPresetApplied(module.getName() + " • " + mode.label(), changed);
    }
}
