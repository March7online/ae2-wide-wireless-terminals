package com.ae2wideterminal.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import com.ae2wideterminal.client.TerminalKind;

import appeng.client.gui.me.items.CraftingTermScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.menu.me.items.WirelessCraftingTermMenu;

public final class WideAe2CraftingTerminalScreen extends CraftingTermScreen<WirelessCraftingTermMenu> {
    public WideAe2CraftingTerminalScreen(WirelessCraftingTermMenu menu, Inventory playerInventory,
            Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
        addToLeftToolbar(WideTerminalScreenSupport.createButton(
                this,
                TerminalKind.AE2_WIRELESS_CRAFTING,
                nextStyle -> new WideAe2CraftingTerminalScreen(menu, playerInventory, title, nextStyle)));
    }
}
