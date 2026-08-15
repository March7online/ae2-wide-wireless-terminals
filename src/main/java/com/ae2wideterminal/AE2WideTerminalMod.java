package com.ae2wideterminal;

import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

@Mod(AE2WideTerminalMod.MOD_ID)
public final class AE2WideTerminalMod {
    public static final String MOD_ID = "ae2wideterminal";

    public AE2WideTerminalMod() {
        ModLoadingContext.get().registerDisplayTest(IExtensionPoint.DisplayTest.IGNORE_ALL_VERSION);
    }
}
