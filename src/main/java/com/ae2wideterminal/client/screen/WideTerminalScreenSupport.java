package com.ae2wideterminal.client.screen;

import net.minecraft.client.Minecraft;

import com.ae2wideterminal.client.TerminalKind;
import com.ae2wideterminal.client.WideTerminalStyleSelector;
import com.ae2wideterminal.client.widget.WideModeButton;
import com.ae2wideterminal.config.WideTerminalClientConfig;

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
        current.storeState();
        WideTerminalClientConfig.toggleAndSave();
        Minecraft.getInstance().setScreen(factory.create(WideTerminalStyleSelector.select(kind)));
    }
}

