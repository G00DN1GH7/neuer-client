package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;
import com.neuer.mixin.client.MinecraftAccessor;
import net.minecraft.world.entity.Entity;

public class NoHitDelay extends Module {

    public NoHitDelay() {
        super("NoHitDelay", Category.Combat, "Removes the extra delay between consecutive attacks.");
    }

    @Override
    public void onTick() {
        if (mc.player == null || mc.level == null) return;
        
        if (mc instanceof MinecraftAccessor accessor) {
            accessor.setMissTime(0);
        }

        for (Entity entity : mc.level.entitiesForRendering()) {
            entity.setInvulnerableTime(0);
        }
    }
}
