package mysticmods.roots.test.decompose;

import mysticmods.roots.api.IRootsAPI;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.mixin.accessor.AccessorMixinLootPool;
import mysticmods.roots.mixin.accessor.AccessorMixinLootTable;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Function;

public class Decomposers {
  public interface Decomposer<FROM, INTO> extends Function<List<FROM>, List<INTO>> {
    default List<INTO> perform(FROM from) {
      return perform(from, null);
    }

    List<INTO> perform(FROM from, @Nullable List<INTO> result);

    default List<INTO> perform (List<FROM> from, @Nullable List<INTO> result) {
      if (result == null) {
        result = new ArrayList<>();
      }

      for (FROM f : from) {
        perform(f, result);
      }

      return result;
    }

    @Override
    default List<INTO> apply(List<FROM> from) {
      List<INTO> result = new ArrayList<>();
      for (FROM f : from) {
        perform(f, result);
      }
      return result;
    }
  }

  public static abstract class DeferringDecomposer<A extends PriorityAssignable & Decomposer<FROM, INTO>, FROM, INTO> implements Decomposer<FROM, INTO> {
    protected final PriorityAssignableMap<A> map;

    public DeferringDecomposer(A none, Class<A> clazz) {
      ClassLoader classLoader = IRootsAPI.class.getClassLoader();
      ServiceLoader<A> serviceLoader = ServiceLoader.load(clazz, classLoader);
      this.map = new PriorityAssignableMap<>(none, serviceLoader);
    }

    @Override
    public List<INTO> perform(FROM from, @Nullable List<INTO> result) {
      if (result == null) {
        result = new ArrayList<>();
      }

      A individual = map.get(from);
      if (individual == null) {
        RootsAPI.LOG.error("Unable to find deferring adapter for class: {}", from.getClass());
        // TODO: Complain
        return result;
      }

      return individual.perform(from, result);
    }
  }

  static class LootTables implements Decomposer<Registry<LootTable>, LootTable> {
    @Override
    public List<LootTable> perform(Registry<LootTable> lootTableRegistry, @Nullable List<LootTable> result) {
      return lootTableRegistry.stream().toList();
    }
  }

  static class LootPools implements Decomposer<LootTable, LootPool> {
    @Override
    public List<LootPool> perform(LootTable lootTables, @Nullable List<LootPool> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      result.addAll(((AccessorMixinLootTable) lootTables).roots$GetPools());
      return result;
    }
  }

  static class LootPoolEntries implements Decomposer<LootPool, LootPoolEntryContainer> {
    @Override
    public List<LootPoolEntryContainer> perform(LootPool lootPool, @Nullable List<LootPoolEntryContainer> result) {
      if (result == null) {
        result = new ArrayList<>();
      }
      result.addAll(((AccessorMixinLootPool) lootPool).roots$GetEntries());
      return result;
    }
  }

  static class LootPoolEntryContainers extends DeferringDecomposer<ILootPoolEntryContainerDecomposer<LootPoolEntryContainer>, LootPoolEntryContainer, ILootPoolEntryContainerDecomposer.ItemRecord> {

    @SuppressWarnings("unchecked")
    public LootPoolEntryContainers() {
      super(ILootPoolEntryContainerDecomposer.NONE, (Class<ILootPoolEntryContainerDecomposer<LootPoolEntryContainer>>) (Class<?>) ILootPoolEntryContainerDecomposer.class);
    }
  }

  static LootTables LOOT_TABLES = new LootTables();
  static LootPools LOOT_POOLS = new LootPools();
  static LootPoolEntries LOOT_POOL_ENTRIES = new LootPoolEntries();
  static LootPoolEntryContainers LOOT_POOL_ENTRY_CONTAINERS = new LootPoolEntryContainers();

  static Function<List<LootTable>, List<ILootPoolEntryContainerDecomposer.ItemRecord>> LOOT_TABLE_INTERNAL = LOOT_POOLS.andThen(LOOT_POOL_ENTRIES).andThen(LOOT_POOL_ENTRY_CONTAINERS);

  static Function<List<Registry<LootTable>>, List<ILootPoolEntryContainerDecomposer.ItemRecord>> ALL_LOOT_INTERIOR = LOOT_TABLES.andThen(LOOT_POOLS).andThen(LOOT_POOL_ENTRIES).andThen(LOOT_POOL_ENTRY_CONTAINERS);

  static Decomposer<Registry<LootTable>, ILootPoolEntryContainerDecomposer.ItemRecord> ALL_LOOT = (lootTables, result) -> ALL_LOOT_INTERIOR.apply(List.of(lootTables));

  static Decomposer<LootTable, ILootPoolEntryContainerDecomposer.ItemRecord> LOOT_TABLE = (lootTable, result) -> LOOT_TABLE_INTERNAL.apply(List.of(lootTable));
}
