package com.neuer.client.glassui.model;

import com.neuer.client.glassui.asset.UiIcon;

public record UiStatCard(
    String label,
    String value,
    String detail,
    float progress,
    UiIcon icon
) {
}
