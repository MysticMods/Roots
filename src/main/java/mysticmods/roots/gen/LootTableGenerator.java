/*
package mysticmods.roots.gen;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableGenerator extends LootTableProvider {
  private static final ResourceLocation HUT = RootsAPI.rl("hut");
  private static final ResourceLocation BARROW = RootsAPI.rl("barrow");
  private static final ResourceLocation STANDING_STONES = RootsAPI.rl("standing_stones");

  private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> tables = ImmutableList.of(Pair.of(ChestLootTables::new, LootContextParamSets.CHEST));

  public LootTableGenerator(PackOutput arg, Set<ResourceKey<LootTable>> set, List<SubProviderEntry> list, CompletableFuture<HolderLookup.Provider> completableFuture) {
    super(arg, set, list, completableFuture);
  }

  @Override
  public List<SubProviderEntry> getTables() {
    return List.of(new ChestLootTables());
  }


  @SuppressWarnings("Duplicates")
  public static class ChestLootTables extends SubProviderEntry {
    @Override
    public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {


    }
  }
}
*/
