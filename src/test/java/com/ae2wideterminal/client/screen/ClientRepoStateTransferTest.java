package com.ae2wideterminal.client.screen;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;

import appeng.menu.me.common.GridInventoryEntry;
import appeng.menu.me.common.IClientRepo;
import net.minecraft.world.item.crafting.Ingredient;

class ClientRepoStateTransferTest {
    @Test
    void copiesAllKnownEntriesAsAFullUpdate() {
        var source = new RecordingRepo(List.of(
                new GridInventoryEntry(1L, null, 0L, 0L, true),
                new GridInventoryEntry(2L, null, 42L, 0L, false)));
        var target = new RecordingRepo(List.of());

        ClientRepoStateTransfer.copy(source, target);

        assertTrue(target.fullUpdate);
        assertEquals(source.entries, target.entries);
    }

    @Test
    void copiesSearchTextAlongsideKnownEntries() {
        var source = new RecordingRepo(List.of());
        var target = new RecordingRepo(List.of());
        var restoredSearch = new AtomicReference<String>();

        ClientRepoStateTransfer.copy(source, "fluix", target, restoredSearch::set);

        assertEquals("fluix", restoredSearch.get());
        assertTrue(target.fullUpdate);
        assertEquals(source.entries, target.entries);
    }

    private static final class RecordingRepo implements IClientRepo {
        private final Set<GridInventoryEntry> entries;
        private boolean fullUpdate;

        private RecordingRepo(Collection<GridInventoryEntry> entries) {
            this.entries = new HashSet<>(entries);
        }

        @Override
        public void handleUpdate(boolean fullUpdate, List<GridInventoryEntry> entries) {
            this.fullUpdate = fullUpdate;
            this.entries.clear();
            this.entries.addAll(entries);
        }

        @Override
        public Set<GridInventoryEntry> getAllEntries() {
            return entries;
        }

        @Override
        public Collection<GridInventoryEntry> getByIngredient(Ingredient ingredient) {
            return entries;
        }
    }
}
