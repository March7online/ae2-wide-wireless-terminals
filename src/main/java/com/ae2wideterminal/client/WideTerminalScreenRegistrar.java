package com.ae2wideterminal.client;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.MenuType;

import de.mari_023.ae2wtlib.wct.WCTMenu;
import de.mari_023.ae2wtlib.wet.WETMenu;

import com.ae2wideterminal.client.screen.WideAe2CraftingTerminalScreen;
import com.ae2wideterminal.client.screen.WideWirelessTerminalScreen;
import com.ae2wideterminal.client.screen.WideWtlibCraftingTerminalScreen;
import com.ae2wideterminal.client.screen.WideWtlibPatternEncodingScreen;

import appeng.init.client.InitScreens;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.MEStorageMenu;
import appeng.menu.me.items.WirelessCraftingTermMenu;

public final class WideTerminalScreenRegistrar {
    private WideTerminalScreenRegistrar() {
    }

    public static boolean tryRegister(MenuType<?> type) {
        if (type == MEStorageMenu.WIRELESS_TYPE) {
            register(MEStorageMenu.WIRELESS_TYPE, TerminalKind.WIRELESS_STORAGE,
                    WideWirelessTerminalScreen::new);
            return true;
        }
        if (type == WirelessCraftingTermMenu.TYPE) {
            register(WirelessCraftingTermMenu.TYPE, TerminalKind.AE2_WIRELESS_CRAFTING,
                    WideAe2CraftingTerminalScreen::new);
            return true;
        }
        if (type == WCTMenu.TYPE) {
            register(WCTMenu.TYPE, TerminalKind.WTLIB_CRAFTING,
                    WideWtlibCraftingTerminalScreen::new);
            return true;
        }
        if (type == WETMenu.TYPE) {
            register(WETMenu.TYPE, TerminalKind.WTLIB_PATTERN_ENCODING,
                    WideWtlibPatternEncodingScreen::new);
            return true;
        }
        return false;
    }

    private static <M extends AEBaseMenu, S extends Screen & MenuAccess<M>> void register(
            MenuType<M> type,
            TerminalKind kind,
            InitScreens.StyledScreenFactory<M, S> factory) {
        MenuScreens.<M, S>register(type, (menu, inventory, title) -> factory.create(
                menu,
                inventory,
                title,
                WideTerminalStyleSelector.select(kind)));
    }
}
