package com.ae2wideterminal.client.widget;

public enum WideModeButtonState {
    SWITCH_TO_WIDE("gui.ae2wideterminal.switch_to_wide", 0),
    SWITCH_TO_NARROW("gui.ae2wideterminal.switch_to_narrow", 16),
    INSUFFICIENT_WIDTH("gui.ae2wideterminal.insufficient_width", 16);

    private final String translationKey;
    private final int textureV;

    WideModeButtonState(String translationKey, int textureV) {
        this.translationKey = translationKey;
        this.textureV = textureV;
    }

    public static WideModeButtonState resolve(boolean widePreferred, boolean wideFits) {
        if (!widePreferred) {
            return SWITCH_TO_WIDE;
        }
        return wideFits ? SWITCH_TO_NARROW : INSUFFICIENT_WIDTH;
    }

    public String translationKey() {
        return translationKey;
    }

    public int textureV() {
        return textureV;
    }
}

