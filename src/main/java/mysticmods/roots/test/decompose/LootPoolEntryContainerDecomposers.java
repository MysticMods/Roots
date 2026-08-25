package mysticmods.roots.test.decompose;

import com.google.auto.service.AutoService;
import mysticmods.roots.mixin.accessor.AccessorMixinCompositeEntryBase;
import mysticmods.roots.mixin.accessor.AccessorMixinLootItem;
import mysticmods.roots.mixin.accessor.AccessorMixinNestedLootTable;
import mysticmods.roots.mixin.accessor.AccessorMixinTagEntry;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.*;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class LootPoolEntryContainerDecomposers {
  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class LootItemDecomposer implements ILootPoolEntryContainerDecomposer<LootItem> {
    @Override
    public Class<?> getAssignableClass() {
      return LootItem.class;
    }

    @Override
    public List<ItemRecord> perform(LootItem lootItem, @Nullable List<ItemRecord> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      result.add(new ItemRecord(((AccessorMixinLootItem) lootItem).roots$GetItem().value()));
      return result;
    }
  }

  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class AlternativesEntryDecomposer implements ILootPoolEntryContainerDecomposer<AlternativesEntry> {

    @Override
    public List<ItemRecord> perform(AlternativesEntry alternativesEntry, @Nullable List<ItemRecord> result) {
      return Decomposers.LOOT_POOL_ENTRY_CONTAINERS.perform(((AccessorMixinCompositeEntryBase) alternativesEntry).roots$getChildren(), result);
    }

    @Override
    public Class<?> getAssignableClass() {
      return AlternativesEntry.class;
    }
  }

  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class EmptyLootItemDecomposer implements ILootPoolEntryContainerDecomposer<EmptyLootItem> {

    @Override
    public List<ItemRecord> perform(EmptyLootItem emptyLootItem, @Nullable List<ItemRecord> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      return result;
    }

    @Override
    public Class<?> getAssignableClass() {
      return EmptyLootItem.class;
    }
  }

  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class EntryGroupDecomposer implements ILootPoolEntryContainerDecomposer<EntryGroup> {

    @Override
    public List<ItemRecord> perform(EntryGroup alternativesEntry, @Nullable List<ItemRecord> result) {
      return Decomposers.LOOT_POOL_ENTRY_CONTAINERS.perform(((AccessorMixinCompositeEntryBase) alternativesEntry).roots$getChildren(), result);
    }

    @Override
    public Class<?> getAssignableClass() {
      return EntryGroup.class;
    }
  }

  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class NestedLootTableDecomposer implements ILootPoolEntryContainerDecomposer<NestedLootTable> {

    @Override
    public List<ItemRecord> perform(NestedLootTable nestedLootTable, @Nullable List<ItemRecord> result) {
      LootTable table =
          ((AccessorMixinNestedLootTable) nestedLootTable).roots$GetContents()
              .map(resourceKey -> ServerLifecycleHooks.getCurrentServer().reloadableRegistries()
                  .getLootTable(resourceKey), Function.identity());
      return Decomposers.LOOT_TABLE.perform(List.of(table), result);
    }

    @Override
    public Class<?> getAssignableClass() {
      return NestedLootTable.class;
    }
  }

  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class SequentialEntryDecomposer implements ILootPoolEntryContainerDecomposer<SequentialEntry> {

    @Override
    public List<ItemRecord> perform(SequentialEntry sequentialEntry, @Nullable List<ItemRecord> result) {
      return Decomposers.LOOT_POOL_ENTRY_CONTAINERS.perform(((AccessorMixinCompositeEntryBase) sequentialEntry).roots$getChildren(), result);
    }

    @Override
    public Class<?> getAssignableClass() {
      return SequentialEntry.class;
    }
  }

  @AutoService(ILootPoolEntryContainerDecomposer.class)
  public static class TagEntryDecomposer implements ILootPoolEntryContainerDecomposer<TagEntry> {

    @Override
    public List<ItemRecord> perform(TagEntry tagEntry, @Nullable List<ItemRecord> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      result.add(new ItemRecord(((AccessorMixinTagEntry) tagEntry).roots$GetTag()));
      return result;
    }

    @Override
    public Class<?> getAssignableClass() {
      return TagEntry.class;
    }
  }
}
