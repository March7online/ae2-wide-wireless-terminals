package com.ae2wideterminal.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class WideTerminalClientConfig {
    private static final ForgeConfigSpec.BooleanValue WIDE_MODE;

    public static final ForgeConfigSpec SPEC;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        WIDE_MODE = builder
                .comment("Use 18 columns when the current GUI width can fit the wide terminal.")
                .define("wideMode", false);
        SPEC = builder.build();
    }

    private WideTerminalClientConfig() {
    }

    public static boolean isWidePreferred() {
        return WIDE_MODE.get();
    }

    public static void toggleAndSave() {
        WIDE_MODE.set(!WIDE_MODE.get());
        WIDE_MODE.save();
    }
}
