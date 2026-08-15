package com.ae2wideterminal.client;

public final class WideModePolicy {
    private WideModePolicy() {
    }

    public static int requiredGuiWidth(int screenWidth, int toolbarWidth, int sideMargin) {
        return screenWidth + toolbarWidth + sideMargin * 2;
    }

    public static boolean useWide(boolean preferred, int guiWidth,
            int screenWidth, int toolbarWidth, int sideMargin) {
        return preferred && guiWidth >= requiredGuiWidth(screenWidth, toolbarWidth, sideMargin);
    }
}

