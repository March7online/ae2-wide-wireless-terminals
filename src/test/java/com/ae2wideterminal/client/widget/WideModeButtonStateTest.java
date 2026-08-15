package com.ae2wideterminal.client.widget;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class WideModeButtonStateTest {
    @Test
    void narrowPreferenceOffersWideMode() {
        assertEquals(WideModeButtonState.SWITCH_TO_WIDE,
                WideModeButtonState.resolve(false, true));
    }

    @Test
    void widePreferenceOffersNarrowModeWhenItFits() {
        assertEquals(WideModeButtonState.SWITCH_TO_NARROW,
                WideModeButtonState.resolve(true, true));
    }

    @Test
    void insufficientSpaceOverridesWidePreferenceTooltip() {
        assertEquals(WideModeButtonState.INSUFFICIENT_WIDTH,
                WideModeButtonState.resolve(true, false));
    }
}
