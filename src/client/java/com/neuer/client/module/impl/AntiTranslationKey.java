package com.neuer.client.module.impl;

import com.neuer.client.module.Category;
import com.neuer.client.module.Module;

public class AntiTranslationKey extends Module {
    public AntiTranslationKey() {
        super("AntiTranslationKey", Category.Misc, "Prevents servers from crashing you with malformed translation strings.");
    }
}