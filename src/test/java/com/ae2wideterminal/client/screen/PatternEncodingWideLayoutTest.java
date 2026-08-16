package com.ae2wideterminal.client.screen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import appeng.parts.encoding.EncodingMode;

class PatternEncodingWideLayoutTest {
    @Test
    void anchorsPatternModeBackgroundToTheWideScreenLeftEdge() {
        assertEquals(90, PatternEncodingWideLayout.resolvePanelLeft(9, 18));
    }

    @Test
    void keepsPatternModeBackgroundAtItsVanillaWidthInWideLayout() {
        assertEquals(126, PatternEncodingWideLayout.resolvePanelWidth(126, 18));
    }

    @Test
    void keepsPatternModeBackgroundAtVanillaPositionInNarrowLayout() {
        assertEquals(9, PatternEncodingWideLayout.resolvePanelLeft(9, 9));
        assertEquals(126, PatternEncodingWideLayout.resolvePanelWidth(126, 9));
    }

    @Test
    void repositionsModeSlotsAfterSwitchingBackToNarrowLayout() {
        assertTrue(PatternEncodingWideLayout.shouldPositionSlots(9, false));
        assertTrue(PatternEncodingWideLayout.shouldPositionSlots(18, false));
        assertFalse(PatternEncodingWideLayout.shouldPositionSlots(18, true));
    }

    @Test
    void refreshesOnlySlotsOwnedByTheCurrentEncodingMode() {
        assertEquals(List.of("CRAFTING_GRID", "CRAFTING_RESULT"), slotIds(EncodingMode.CRAFTING));
        assertEquals(List.of("PROCESSING_INPUTS", "PROCESSING_OUTPUTS"), slotIds(EncodingMode.PROCESSING));
        assertEquals(List.of(
                "SMITHING_TABLE_TEMPLATE",
                "SMITHING_TABLE_BASE",
                "SMITHING_TABLE_ADDITION",
                "SMITHING_TABLE_RESULT"), slotIds(EncodingMode.SMITHING_TABLE));
        assertEquals(List.of("STONECUTTING_INPUT"), slotIds(EncodingMode.STONECUTTING));
    }

    private static List<String> slotIds(EncodingMode mode) {
        return PatternEncodingWideLayout.slotSemanticsFor(mode).stream()
                .map(semantic -> semantic.id())
                .toList();
    }
}
