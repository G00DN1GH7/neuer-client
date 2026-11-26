package com.neuer.mixin.client;

import com.neuer.client.gui.NeuerTitleScreen;
import com.neuer.client.module.impl.Menu;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class TitleScreenReplaceMixin {

    @ModifyVariable(method = "setScreen", at = @At("HEAD"), argsOnly = true)
    private Screen replaceTitleScreen(Screen screen) {
        if (screen == null) {
            return null;
        }
        if (!Menu.isCustomMenuEnabled()) {
            if (screen instanceof NeuerTitleScreen) {
                return new TitleScreen();
            }
            return screen;
        }
        if (screen.getClass() == TitleScreen.class) {
            return new NeuerTitleScreen();
        }
        return screen;
    }
}
