package com.ae2wideterminal.mixin.client;

import net.minecraft.client.Minecraft;

import com.ae2wideterminal.client.screen.WideWtlibPatternEncodingScreen;

import appeng.client.gui.me.items.CraftingEncodingPanel;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import appeng.client.gui.me.items.SmithingTableEncodingPanel;
import appeng.client.gui.me.items.StonecuttingEncodingPanel;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = {
        CraftingEncodingPanel.class,
        ProcessingEncodingPanel.class,
        SmithingTableEncodingPanel.class,
        StonecuttingEncodingPanel.class
}, remap = false)
public abstract class EncodingModePanelPositionMixin {
    @ModifyConstant(method = "drawBackgroundLayer", constant = @Constant(intValue = 9))
    private int ae2wideterminal$moveModePanelToWidePosition(int originalLeft) {
        if (Minecraft.getInstance().screen instanceof WideWtlibPatternEncodingScreen screen) {
            return screen.resolvePatternPanelLeft(originalLeft);
        }
        return originalLeft;
    }
}
