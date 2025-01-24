package mysticmods.roots.gen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.loot.modifiers.AddGrassDropsModifier;
import mysticmods.roots.loot.predicates.LootItemBlockTagCondition;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class RootsGlobalLootModifierProvider extends GlobalLootModifierProvider {
  public RootsGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, RootsAPI.MODID);
  }

  private LootItemCondition[] getShortConditions(float chance) {
    return new LootItemCondition[]{
        LootItemRandomChanceCondition.randomChance(chance).build(),
        LootItemBlockTagCondition.tag(RootsTags.Blocks.SHORT_GRASS),
        InvertedLootItemCondition.invert(
            MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR))
        ).build()
    };
  }

  private LootItemCondition[] getTallConditions(float chance) {
    return new LootItemCondition[]{
        LootItemRandomChanceCondition.randomChance(chance).build(),
        LootItemBlockTagCondition.tag(RootsTags.Blocks.TALL_GRASS),
        LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
            .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)).build(),
        InvertedLootItemCondition.invert(
            MatchTool.toolMatches(ItemPredicate.Builder.item().of(Tags.Items.TOOLS_SHEAR))
        ).build()
    };
  }

  @Override
  protected void start() {
    this.add("aubergine_from_short_grass", new AddGrassDropsModifier(getShortConditions(0.01f), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_short_grass", new AddGrassDropsModifier(getShortConditions(0.008f), ModItems.GROVE_SPORES));
    this.add("wildroot_from_short_grass", new AddGrassDropsModifier(getShortConditions(0.004f), ModItems.WILDROOT));

    this.add("aubergine_from_tall_grass", new AddGrassDropsModifier(getTallConditions(0.01f), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_tall_grass", new AddGrassDropsModifier(getTallConditions(0.008f), ModItems.GROVE_SPORES));
    this.add("wildroot_from_tall_grass", new AddGrassDropsModifier(getTallConditions(0.004f), ModItems.WILDROOT));
  }
}
