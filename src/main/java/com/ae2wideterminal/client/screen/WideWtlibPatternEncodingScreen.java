package com.ae2wideterminal.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import de.mari_023.ae2wtlib.wet.WETMenu;
import de.mari_023.ae2wtlib.wet.WETScreen;

import com.ae2wideterminal.client.TerminalKind;

import appeng.client.gui.style.ScreenStyle;

public final class WideWtlibPatternEncodingScreen extends WETScreen {
    public WideWtlibPatternEncodingScreen(WETMenu menu, Inventory playerInventory,
            Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        addToLeftToolbar(WideTerminalScreenSupport.createButton(
                this,
                TerminalKind.WTLIB_PATTERN_ENCODING,
                nextStyle -> new WideWtlibPatternEncodingScreen(menu, playerInventory, title, nextStyle)));
    }
}

