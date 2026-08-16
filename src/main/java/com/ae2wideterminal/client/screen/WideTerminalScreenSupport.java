package com.ae2wideterminal.client.screen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

import com.ae2wideterminal.client.TerminalKind;
import com.ae2wideterminal.client.WideTerminalStyleSelector;
import com.ae2wideterminal.client.widget.WideModeButton;
import com.ae2wideterminal.config.WideTerminalClientConfig;
import com.ae2wideterminal.mixin.client.MEStorageScreenSearchAccess;

import appeng.client.gui.me.common.MEStorageScreen;

public final class WideTerminalScreenSupport {
    private WideTerminalScreenSupport() {
    }

    public static WideModeButton createButton(MEStorageScreen<?> current,
            TerminalKind kind, WideScreenFactory factory) {
        return new WideModeButton(() -> toggle(current, kind, factory), kind);
    }

    private static void toggle(MEStorageScreen<?> current,
            TerminalKind kind, WideScreenFactory factory) {
        String previousSearch = current instanceof MEStorageScreenSearchAccess access
                ? access.ae2wideterminal$getSearchField().getValue()
                : "";
        current.storeState();
        var previousRepo = current.getMenu().getClientRepo();
        WideTerminalClientConfig.toggleAndSave();
        Screen next = factory.create(WideTerminalStyleSelector.select(kind));
        if (next instanceof MEStorageScreen<?> nextStorage
                && nextStorage instanceof MEStorageScreenSearchAccess access) {
            ClientRepoStateTransfer.copy(previousRepo, previousSearch,
                    nextStorage.getMenu().getClientRepo(), access::ae2wideterminal$setSearchText);
        }
        Minecraft.getInstance().setScreen(next);
    }
}
