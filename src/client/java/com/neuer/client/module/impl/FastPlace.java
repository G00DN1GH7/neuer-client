package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class FastPlace extends Module {
    public static FastPlace INSTANCE;

    public FastPlace() {
        super("FastPlace", Category.World, "Removes the right-click delay for placing blocks.");
        INSTANCE = this;
    }
}