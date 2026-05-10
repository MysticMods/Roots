package mysticmods.roots.test.decompose;

import mysticmods.roots.mixin.accessor.AccessorMixinLootPool;
import mysticmods.roots.mixin.accessor.AccessorMixinLootTable;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Decomposers {
  public interface Decomposer<FROM, INTO> extends Function<List<FROM>, List<INTO>> {
    default List<INTO> perform(FROM from) {
      return perform(from, null);
    }

    List<INTO> perform(FROM from, @Nullable List<INTO> result);

    @Override
    default List<INTO> apply(List<FROM> from) {
      List<INTO> result = new ArrayList<>();
      for (FROM f : from) {
        perform(f, result);
      }
      return result;
    }
  }

  public static abstract class DeferringDecomposer<FROM, INTO> implements Decomposer<FROM, INTO> {

  }

  public static class LootTables implements Decomposer<Registry<LootTable>, LootTable> {
    @Override
    public List<LootTable> perform(Registry<LootTable> lootTableRegistry, @Nullable List<LootTable> result) {
      return lootTableRegistry.stream().toList();
    }
  }

  public static class LootPools implements Decomposer<LootTables, LootPool> {
    @Override
    public List<LootPool> perform(LootTables lootTables, @Nullable List<LootPool> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      result.addAll(((AccessorMixinLootTable) lootTables).rootsGetPools());
      return result;
    }
  }

  public static class LootPoolEntry implements Decomposer<LootPool, LootPoolEntryContainer> {
    @Override
    public List<LootPoolEntryContainer> perform(LootPool lootPool, @Nullable List<LootPoolEntryContainer> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      result.addAll(((AccessorMixinLootPool) lootPool).rootsGetEntries());
      return result;
    }
  }
}
