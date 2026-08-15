package com.ae2wideterminal.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class WideModePolicyTest {
    @Test
    void wideModeRequiresPreferenceAndEnoughSpace() {
        assertFalse(WideModePolicy.useWide(false, 500, 357, 24, 12));
        assertTrue(WideModePolicy.useWide(true, 405, 357, 24, 12));
        assertFalse(WideModePolicy.useWide(true, 404, 357, 24, 12));
    }

    @Test
    void requiredWidthIncludesToolbarAndBothMargins() {
        assertEquals(405, WideModePolicy.requiredGuiWidth(357, 24, 12));
        assertEquals(410, WideModePolicy.requiredGuiWidth(362, 24, 12));
    }
}
