package com.ae2wideterminal.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TerminalKindTest {
    @Test
    void onlyApprovedKindsHaveWideStyles() {
        assertEquals(4, TerminalKind.values().length);
        assertEquals(357, TerminalKind.WIRELESS_STORAGE.wideScreenWidth());
        assertEquals(357, TerminalKind.AE2_WIRELESS_CRAFTING.wideScreenWidth());
        assertEquals(362, TerminalKind.WTLIB_CRAFTING.wideScreenWidth());
        assertEquals(357, TerminalKind.WTLIB_PATTERN_ENCODING.wideScreenWidth());
        assertFalse(Arrays.stream(TerminalKind.values())
                .anyMatch(kind -> kind.name().contains("PATTERN_ACCESS")));
    }

    @Test
    void stylePathsAreAbsoluteAndDistinct() {
        for (TerminalKind kind : TerminalKind.values()) {
            assertTrue(kind.narrowStylePath().startsWith("/screens/"));
            assertTrue(kind.wideStylePath().startsWith("/screens/ae2wideterminal/"));
            assertNotEquals(kind.narrowStylePath(), kind.wideStylePath());
        }
    }
}
