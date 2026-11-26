package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.glassui.screen.GlassUiScreen;
import com.neuer.client.gui.MenuConfigScreen;
import com.neuer.client.macro.MacroManager;
import com.neuer.client.module.Module;
import com.neuer.client.module.ModuleManager;
import com.neuer.client.module.impl.GUI;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.breadloaf.imguimc.imgui.ImguiLoader;
import xyz.breadloaf.imguimc.imgui.SdlImGuiPlatform;

@Mixin(KeyboardHandler.class)
public class KeyboardHandlerMixin {

    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void onKeyPress(long window, int action, KeyEvent event, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Screen current = mc.gui.screen();
        if (action == 1 && current instanceof GlassUiScreen gs && gs.isListeningForBind()) {
            if (gs.onRawKeyForBind(event.key())) {
                ci.cancel();
                return;
            }
        }
        if (action == 1) {
            int guiKey = resolveGuiKeybind();
            if (event.key() == guiKey) {
                if (current instanceof TitleScreen) {
                    mc.setScreenAndShow(new MenuConfigScreen(current));
                    ci.cancel();
                    return;
                }
                if (current == null && mc.player != null && !isBypassActive()) {
                    GUI.openClickGui();
                    ci.cancel();
                    return;
                }
            }
        }
        if (current != null) {
            return;
        }
        if (MacroManager.isRecording()) {
            if (action == 1) {
                MacroManager.recordKey(event.key(), true, window);
            } else if (action == 0) {
                MacroManager.recordKey(event.key(), false, window);
            }
        }
    }

    @Inject(method = "charTyped", at = @At("HEAD"))
    private void onCharTyped(long window, CharacterEvent event, CallbackInfo ci) {
        Minecraft mc = Minecraft.getInstance();
        Screen current = mc.gui.screen();
        boolean binding = current instanceof GlassUiScreen gs && gs.isListeningForBind();
        if (ImguiLoader.wantsTextInput() && !binding) {
            SdlImGuiPlatform.feedChar(event.codepoint());
        }
    }

    private boolean isBypassActive() {
        NeuerClient client = NeuerClient.INSTANCE;
        if (client != null && client.moduleManager != null) {
            ModuleManager mm = client.moduleManager;
            Module bypass = mm.getModule("Bypass");
            return bypass != null && bypass.isToggled();
        }
        return false;
    }

    private int resolveGuiKeybind() {
        NeuerClient client = NeuerClient.INSTANCE;
        if (client != null && client.moduleManager != null) {
            ModuleManager mm = client.moduleManager;
            Module guiMod = mm.getModule("GUI");
            if (guiMod != null && guiMod.getKeyBind() > 0) {
                return guiMod.getKeyBind();
            }
        }
        return InputConstants.KEY_RSHIFT;
    }
}
