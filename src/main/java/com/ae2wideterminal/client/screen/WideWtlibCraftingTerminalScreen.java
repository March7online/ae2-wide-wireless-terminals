package com.ae2wideterminal.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import de.mari_023.ae2wtlib.wct.WCTMenu;
import de.mari_023.ae2wtlib.wct.WCTScreen;

import com.ae2wideterminal.client.TerminalKind;

import appeng.client.gui.style.ScreenStyle;

public final class WideWtlibCraftingTerminalScreen extends WCTScreen {
    public WideWtlibCraftingTerminalScreen(WCTMenu menu, Inventory playerInventory,
            Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        addToLeftToolbar(WideTerminalScreenSupport.createButton(
                this,
                TerminalKind.WTLIB_CRAFTING,
                nextStyle -> new WideWtlibCraftingTerminalScreen(menu, playerInventory, title, nextStyle)));
    }
}
