package mysticmods.roots.gen.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.loot.conditions.ForagingRandomChanceCondition;
import mysticmods.roots.loot.conditions.LootItemBlockTagCondition;
import mysticmods.roots.loot.conditions.WaterloggedBlockCondition;
import mysticmods.roots.loot.modifiers.AddGrassDropsModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RootsGlobalLootModifierProvider extends GlobalLootModifierProvider {
  public RootsGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, RootsAPI.MODID);
  }

  private LootItemCondition[] getGrassConditions(TagKey<Block> tag, float chance, boolean tall) {
    List<LootItemCondition> conditions = new ArrayList<>();
    conditions.add(LootItemRandomChanceCondition.randomChance(chance).build());
    conditions.add(LootItemBlockTagCondition.tag(tag));
/*    if (tall) {
      conditions.add(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)).build());
    }*/
    conditions.add(InvertedLootItemCondition.invert(
        CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG)
    ).build());
    return conditions.toArray(LootItemCondition[]::new);
  }

  private LootItemCondition[] getForagingConditions(TagKey<Block> tag, float initialChance, boolean tall, boolean wet) {
    List<LootItemCondition> conditions = new ArrayList<>();
    conditions.add(ForagingRandomChanceCondition.randomChance(initialChance).build());
    conditions.add(LootItemBlockTagCondition.tag(tag));
/*    if (tall) {
      conditions.add(LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.TALL_GRASS)
          .setProperties(StatePropertiesPredicate.Builder.properties()
              .hasProperty(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)).build());
    }*/
    if (wet) {
      conditions.add(WaterloggedBlockCondition.waterlogged().build());
    } else {
      conditions.add(InvertedLootItemCondition.invert(WaterloggedBlockCondition.waterlogged()).build());
    }
    conditions.add(InvertedLootItemCondition.invert(
        CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG)
    ).build());
    return conditions.toArray(LootItemCondition[]::new);
  }


  @Override
  protected void start() {
    this.add("aubergine_from_short_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.SHORT_GRASS, 0.01f, false), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_short_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.SHORT_GRASS, 0.008f, false), ModItems.GROVE_SPORES));
    this.add("wildroot_from_short_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.SHORT_GRASS, 0.004f, false), ModItems.WILDROOT));

    this.add("aubergine_from_tall_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.TALL_GRASS, 0.01f, true), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_tall_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.TALL_GRASS, 0.008f, true), ModItems.GROVE_SPORES));
    this.add("wildroot_from_tall_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.TALL_GRASS, 0.004f, true), ModItems.WILDROOT));

    this.add("grove_spores_from_forageable_single_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS, 0.01f, false, false), ModItems.GROVE_SPORES));
    this.add("grove_spores_from_waterlogged_foreagable_single_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS, 0.03f, false, true), ModItems.GROVE_SPORES));
    this.add("grove_spores_from_foragable_double_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, 0.01f, true, false), ModItems.GROVE_SPORES));
    this.add("grove_spores_from_waterlogged_foreagable_double_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, 0.03f, true, true), ModItems.GROVE_SPORES));

    this.add("wildroot_from_forageable_single_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS, 0.01f, false, false), ModItems.WILDROOT));
    this.add("wildroot_from_foragable_double_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, 0.01f, true, false), ModItems.WILDROOT));
  }
}
