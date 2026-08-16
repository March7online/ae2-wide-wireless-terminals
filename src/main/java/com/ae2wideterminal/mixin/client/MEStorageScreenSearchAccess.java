package com.ae2wideterminal.mixin.client;

import appeng.client.gui.me.common.MEStorageScreen;
import appeng.client.gui.widgets.AETextField;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = MEStorageScreen.class, remap = false)
public interface MEStorageScreenSearchAccess {
    @Accessor("searchField")
    AETextField ae2wideterminal$getSearchField();

    @Invoker("setSearchText")
    void ae2wideterminal$setSearchText(String searchText);
}
