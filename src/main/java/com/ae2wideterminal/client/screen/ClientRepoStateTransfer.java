package com.ae2wideterminal.client.screen;

import java.util.ArrayList;
import java.util.function.Consumer;

import appeng.menu.me.common.IClientRepo;

final class ClientRepoStateTransfer {
    private ClientRepoStateTransfer() {
    }

    static void copy(IClientRepo source, IClientRepo target) {
        if (source == null || target == null || source == target) {
            return;
        }

        target.handleUpdate(true, new ArrayList<>(source.getAllEntries()));
    }

    static void copy(IClientRepo source, String searchText, IClientRepo target,
            Consumer<String> restoreSearch) {
        copy(source, target);
        if (restoreSearch != null) {
            restoreSearch.accept(searchText == null ? "" : searchText);
        }
    }
}
