package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class HandView extends Module {
    public static HandView INSTANCE;

    public HandView() {
        super("HandView", Category.Render, "Alters the scale and position of items in your hand.");
        INSTANCE = this;
    }
}