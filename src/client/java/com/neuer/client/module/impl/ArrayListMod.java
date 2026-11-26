package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class ArrayListMod extends Module {
    public ArrayListMod() {
        super("ArrayList", Category.HUD, "Lists every enabled module in a clean HUD column.");
        this.toggle();
    }
}