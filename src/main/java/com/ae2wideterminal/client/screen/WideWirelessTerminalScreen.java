package com.ae2wideterminal.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import com.ae2wideterminal.client.TerminalKind;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.common.MEStorageMenu;

public final class WideWirelessTerminalScreen extends MEStorageScreen<MEStorageMenu> {
    public WideWirelessTerminalScreen(MEStorageMenu menu, Inventory playerInventory,
            Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        addToLeftToolbar(WideTerminalScreenSupport.createButton(
                this,
                TerminalKind.WIRELESS_STORAGE,
                nextStyle -> new WideWirelessTerminalScreen(menu, playerInventory, title, nextStyle)));
    }
}
