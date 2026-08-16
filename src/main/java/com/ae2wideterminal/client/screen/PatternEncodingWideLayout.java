package com.ae2wideterminal.client.screen;

import java.util.List;

import appeng.menu.SlotSemantic;
import appeng.menu.SlotSemantics;
import appeng.parts.encoding.EncodingMode;

public final class PatternEncodingWideLayout {
    private static final int NARROW_COLUMNS = 9;
    private static final int SLOT_PITCH = 18;

    private PatternEncodingWideLayout() {
    }

    public static int resolvePanelLeft(int originalLeft, int slotsPerRow) {
        int extraColumns = Math.max(0, slotsPerRow - NARROW_COLUMNS);
        return originalLeft + extraColumns * SLOT_PITCH / 2;
    }

    public static int resolvePanelWidth(int originalWidth, int slotsPerRow) {
        return originalWidth;
    }

    public static boolean usesWidePanel(int slotsPerRow) {
        return slotsPerRow > NARROW_COLUMNS;
    }

    public static boolean shouldPositionSlots(int slotsPerRow, boolean alreadyPositioned) {
        return slotsPerRow >= NARROW_COLUMNS && !alreadyPositioned;
    }

    public static List<SlotSemantic> slotSemanticsFor(EncodingMode mode) {
        return switch (mode) {
            case CRAFTING -> List.of(
                    SlotSemantics.CRAFTING_GRID,
                    SlotSemantics.CRAFTING_RESULT);
            case PROCESSING -> List.of(
                    SlotSemantics.PROCESSING_INPUTS,
                    SlotSemantics.PROCESSING_OUTPUTS);
            case SMITHING_TABLE -> List.of(
                    SlotSemantics.SMITHING_TABLE_TEMPLATE,
                    SlotSemantics.SMITHING_TABLE_BASE,
                    SlotSemantics.SMITHING_TABLE_ADDITION,
                    SlotSemantics.SMITHING_TABLE_RESULT);
            case STONECUTTING -> List.of(SlotSemantics.STONECUTTING_INPUT);
        };
    }
}
