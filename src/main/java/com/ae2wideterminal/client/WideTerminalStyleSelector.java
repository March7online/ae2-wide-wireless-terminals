package com.ae2wideterminal.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;

import com.ae2wideterminal.config.WideTerminalClientConfig;

import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.style.StyleManager;

public final class WideTerminalStyleSelector {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int LEFT_TOOLBAR_WIDTH = 24;
    private static final int SIDE_MARGIN = 12;

    private WideTerminalStyleSelector() {
    }

    public static boolean canUseWide(TerminalKind kind) {
        return WideModePolicy.useWide(
                true,
                Minecraft.getInstance().getWindow().getGuiScaledWidth(),
                kind.wideScreenWidth(),
                LEFT_TOOLBAR_WIDTH,
                SIDE_MARGIN);
    }

    public static boolean isWideActive(TerminalKind kind) {
        return WideTerminalClientConfig.isWidePreferred() && canUseWide(kind);
    }

    public static ScreenStyle select(TerminalKind kind) {
        String path = isWideActive(kind) ? kind.wideStylePath() : kind.narrowStylePath();
        try {
            return StyleManager.loadStyleDoc(path);
        } catch (RuntimeException error) {
            LOGGER.error("Failed to load terminal style {} for {}", path, kind, error);
            return StyleManager.loadStyleDoc(kind.narrowStylePath());
        }
    }
}
