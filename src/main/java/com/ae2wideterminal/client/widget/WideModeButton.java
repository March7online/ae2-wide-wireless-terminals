package com.ae2wideterminal.client.widget;

import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.ae2wideterminal.AE2WideTerminalMod;
import com.ae2wideterminal.client.TerminalKind;
import com.ae2wideterminal.client.WideTerminalStyleSelector;
import com.ae2wideterminal.config.WideTerminalClientConfig;

import appeng.client.gui.Icon;
import appeng.client.gui.widgets.ITooltip;

public final class WideModeButton extends Button implements ITooltip {
    private static final ResourceLocation ICONS = ResourceLocation.fromNamespaceAndPath(
            AE2WideTerminalMod.MOD_ID,
            "textures/gui/wide_mode.png");

    private final TerminalKind kind;

    public WideModeButton(Runnable toggle, TerminalKind kind) {
        super(0, 0, 16, 16, message(kind), button -> toggle.run(), DEFAULT_NARRATION);
        this.kind = kind;
    }

    private static Component message(TerminalKind kind) {
        return Component.translatable(state(kind).translationKey());
    }

    private static WideModeButtonState state(TerminalKind kind) {
        return WideModeButtonState.resolve(
                WideTerminalClientConfig.isWidePreferred(),
                WideTerminalStyleSelector.canUseWide(kind));
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        Icon.TOOLBAR_BUTTON_BACKGROUND.getBlitter().dest(getX(), getY()).blit(graphics);
        graphics.blit(ICONS, getX(), getY(), 0, state(kind).textureV(), 16, 16, 16, 32);
        RenderSystem.enableDepthTest();
    }

    @Override
    public List<Component> getTooltipMessage() {
        return List.of(getMessage());
    }

    @Override
    public Rect2i getTooltipArea() {
        return new Rect2i(getX(), getY(), 16, 16);
    }

    @Override
    public boolean isTooltipAreaVisible() {
        return visible;
    }
}
