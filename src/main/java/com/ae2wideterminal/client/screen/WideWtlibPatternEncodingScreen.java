package com.ae2wideterminal.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import de.mari_023.ae2wtlib.wet.WETMenu;
import de.mari_023.ae2wtlib.wet.WETScreen;

import com.ae2wideterminal.client.TerminalKind;

import appeng.client.gui.style.ScreenStyle;

public final class WideWtlibPatternEncodingScreen extends WETScreen {
    private final int slotsPerRow;
    private boolean initialSlotsPositioned;

    public WideWtlibPatternEncodingScreen(WETMenu menu, Inventory playerInventory,
            Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        this.slotsPerRow = style.getTerminalStyle().getSlotsPerRow();
        addToLeftToolbar(WideTerminalScreenSupport.createButton(
                this,
                TerminalKind.WTLIB_PATTERN_ENCODING,
                nextStyle -> new WideWtlibPatternEncodingScreen(menu, playerInventory, title, nextStyle)));
    }

    public int resolvePatternPanelLeft(int originalLeft) {
        return PatternEncodingWideLayout.resolvePanelLeft(originalLeft, slotsPerRow);
    }

    public int resolvePatternPanelWidth(int originalWidth) {
        return PatternEncodingWideLayout.resolvePanelWidth(originalWidth, slotsPerRow);
    }

    public boolean usesWidePatternPanel() {
        return PatternEncodingWideLayout.usesWidePanel(slotsPerRow);
    }

    @Override
    protected void updateBeforeRender() {
        super.updateBeforeRender();

        if (PatternEncodingWideLayout.shouldPositionSlots(slotsPerRow, initialSlotsPositioned)) {
            PatternEncodingWideLayout.slotSemanticsFor(getMenu().getMode())
                    .forEach(this::repositionSlots);
            initialSlotsPositioned = true;
        }
    }
}
