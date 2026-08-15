package com.ae2wideterminal.client.screen;

import net.minecraft.client.gui.screens.Screen;

import appeng.client.gui.style.ScreenStyle;

@FunctionalInterface
public interface WideScreenFactory {
    Screen create(ScreenStyle style);
}

