package com.ae2wideterminal.client;

public enum TerminalKind {
    WIRELESS_STORAGE(
            "/screens/terminals/wireless_terminal.json",
            "/screens/ae2wideterminal/wireless_terminal_wide.json",
            357),
    AE2_WIRELESS_CRAFTING(
            "/screens/terminals/crafting_terminal.json",
            "/screens/ae2wideterminal/ae2_crafting_terminal_wide.json",
            357),
    WTLIB_CRAFTING(
            "/screens/wtlib/wireless_crafting_terminal.json",
            "/screens/ae2wideterminal/wtlib_crafting_terminal_wide.json",
            362),
    WTLIB_PATTERN_ENCODING(
            "/screens/wtlib/wireless_pattern_encoding_terminal.json",
            "/screens/ae2wideterminal/wtlib_pattern_encoding_terminal_wide.json",
            357);

    private final String narrowStylePath;
    private final String wideStylePath;
    private final int wideScreenWidth;

    TerminalKind(String narrowStylePath, String wideStylePath, int wideScreenWidth) {
        this.narrowStylePath = narrowStylePath;
        this.wideStylePath = wideStylePath;
        this.wideScreenWidth = wideScreenWidth;
    }

    public String narrowStylePath() {
        return narrowStylePath;
    }

    public String wideStylePath() {
        return wideStylePath;
    }

    public int wideScreenWidth() {
        return wideScreenWidth;
    }
}
