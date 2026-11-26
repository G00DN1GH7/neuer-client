package com.neuer.mixin.client;

import com.neuer.client.NeuerClient;
import com.neuer.client.module.impl.ESP;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShaderManager;getPostChain(Lnet/minecraft/resources/Identifier;Ljava/util/Set;)Lnet/minecraft/client/renderer/PostChain;"), index = 0)
    private Identifier swapOutlineChain(Identifier id) {
        if (NeuerClient.INSTANCE == null || NeuerClient.INSTANCE.moduleManager == null) return id;
        if (NeuerClient.INSTANCE.moduleManager.getModule("ESP") instanceof ESP esp && esp.isShaderMode()) {
            return esp.getShaderChainId();
        }
        if (com.neuer.client.module.impl.SelfShader.INSTANCE != null
                && com.neuer.client.module.impl.SelfShader.INSTANCE.body()) {
            return com.neuer.client.module.impl.SelfShader.INSTANCE.shaderChainId();
        }
        return id;
    }
}
