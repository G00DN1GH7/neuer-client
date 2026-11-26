package com.neuer.client.glassui.model;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;

/**
 * Stacks several custom sections into the single slot a card or tab provides.
 * Heights add up, each part renders at its own offset, and input is offered to
 * every part in order (each part hit-tests with screen coordinates).
 */
public final class CompositeSection implements UiCustomSection {
    private final List<UiCustomSection> parts;

    private CompositeSection(List<UiCustomSection> parts) {
        this.parts = List.copyOf(parts);
    }

    /** Returns null when nothing is supplied, the single part when only one is, else a stack. */
    public static UiCustomSection combine(UiCustomSection... sections) {
        List<UiCustomSection> present = new ArrayList<>();
        if (sections != null) {
            for (UiCustomSection section : sections) {
                if (section != null) {
                    present.add(section);
                }
            }
        }
        if (present.isEmpty()) {
            return null;
        }
        if (present.size() == 1) {
            return present.get(0);
        }
        return new CompositeSection(present);
    }

    @Override
    public float measureHeight(UiSectionMeasureContext context) {
        float total = 0f;
        for (UiCustomSection part : parts) {
            total += part.measureHeight(context);
        }
        return total;
    }

    @Override
    public void render(UiSectionRenderContext context) {
        float y = context.startY();
        for (UiCustomSection part : parts) {
            UiSectionRenderContext shifted = new UiSectionRenderContext(
                context.graphics(),
                context.scroller(),
                context.areaX(),
                context.areaW(),
                y,
                context.viewTop(),
                context.viewBottom(),
                context.mouseX(),
                context.mouseY(),
                context.animDt(),
                context.alpha(),
                context.interactive(),
                context.actions()
            );
            part.render(shifted);
            y += part.measureHeight(new UiSectionMeasureContext(context.areaW(), context.theme()));
        }
    }

    @Override
    public boolean onMouseClicked(UiScreenActions actions, float localX, float localY, int button) {
        boolean handled = false;
        for (UiCustomSection part : parts) {
            if (part.onMouseClicked(actions, localX, localY, button)) {
                handled = true;
            }
        }
        return handled;
    }

    @Override
    public boolean onKeyPressed(UiScreenActions actions, KeyEvent event) {
        for (UiCustomSection part : parts) {
            if (part.onKeyPressed(actions, event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCharTyped(UiScreenActions actions, CharacterEvent event) {
        for (UiCustomSection part : parts) {
            if (part.onCharTyped(actions, event)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isTextInputFocused() {
        for (UiCustomSection part : parts) {
            if (part.isTextInputFocused()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void clearFocus(UiScreenActions actions) {
        for (UiCustomSection part : parts) {
            part.clearFocus(actions);
        }
    }

    @Override
    public void clearHitboxes() {
        for (UiCustomSection part : parts) {
            part.clearHitboxes();
        }
    }
}
