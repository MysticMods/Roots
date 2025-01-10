package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.loot.modifiers.AddGrassDropsModifier;
import mysticmods.roots.loot.predicates.LootItemBlockTagCondition;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class RootsGlobalLootModifierProvider extends GlobalLootModifierProvider {
  public RootsGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, RootsAPI.MODID);
  }

  private LootItemCondition[] getConditions(float chance) {
    return new LootItemCondition[]{
        LootItemRandomChanceCondition.randomChance(chance).build(),
        LootItemBlockTagCondition.tag(RootsTags.Blocks.GRASS),
        InvertedLootItemCondition.invert(
            MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR))
        ).build()
    };
  }

  @Override
  protected void start() {
    this.add("aubergine_from_grass", new AddGrassDropsModifier(getConditions(0.01f), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_grass", new AddGrassDropsModifier(getConditions(0.008f), ModItems.GROVE_SPORES));
    this.add("wildroot_from_grass", new AddGrassDropsModifier(getConditions(0.004f), ModItems.WILDROOT));
  }
}
