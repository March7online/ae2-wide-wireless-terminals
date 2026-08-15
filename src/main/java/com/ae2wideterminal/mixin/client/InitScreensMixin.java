package com.ae2wideterminal.mixin.client;

import net.minecraft.world.inventory.MenuType;

import com.ae2wideterminal.client.WideTerminalScreenRegistrar;

import appeng.init.client.InitScreens;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InitScreens.class, remap = false)
abstract class InitScreensMixin {
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private static void replaceTargetScreen(MenuType<?> type,
            InitScreens.StyledScreenFactory<?, ?> originalFactory,
            String originalStylePath,
            CallbackInfo callback) {
        if (WideTerminalScreenRegistrar.tryRegister(type)) {
            callback.cancel();
        }
    }
}

