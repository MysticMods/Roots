package mysticmods.roots.gen.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.loot.conditions.*;
import mysticmods.roots.loot.modifiers.AddGrassDropsModifier;
import mysticmods.roots.loot.modifiers.ElementalCropExtraDropsModifier;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.EntityTypePredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.InvertedLootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.CanItemPerformAbility;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RootsGlobalLootModifierProvider extends GlobalLootModifierProvider {
  public RootsGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, RootsAPI.MODID);
  }

  private LootItemCondition[] getCropExtraConditions () {
    List<LootItemCondition> conditions = new ArrayList<>();
    conditions.add(LootItemBlockBelowTagCondition.tag(RootsTags.Blocks.ELEMENTAL_SOIL));
    conditions.add(LootItemBlockTagCondition.tag(BlockTags.CROPS));
    conditions.add(new ConfigSpecificLootCondition("elemental_crop_extra_drops"));
    // TODO: Only fully grown crops, whoops
    return conditions.toArray(LootItemCondition[]::new);
  }

  private LootItemCondition[] getGrassConditions(TagKey<Block> tag, float chance, @Nullable String configName) {
    List<LootItemCondition> conditions = new ArrayList<>();
    conditions.add(LootItemRandomChanceCondition.randomChance(chance).build());
    conditions.add(LootItemBlockTagCondition.tag(tag));
    if (configName != null) {
      conditions.add(new ConfigSpecificLootCondition(configName));
    }
    conditions.add(InvertedLootItemCondition.invert(
        CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG)
    ).build());
    return conditions.toArray(LootItemCondition[]::new);
  }

  private LootItemCondition[] getForagingConditions(TagKey<Block> tag, float initialChance, boolean wet, @Nullable String configName) {
    List<LootItemCondition> conditions = new ArrayList<>();
    conditions.add(ForagingRandomChanceCondition.randomChance(initialChance).build());
    conditions.add(LootItemBlockTagCondition.tag(tag));
    if (wet) {
      conditions.add(WaterloggedBlockCondition.waterlogged().build());
    } else {
      conditions.add(InvertedLootItemCondition.invert(WaterloggedBlockCondition.waterlogged()).build());
    }
    if (configName != null) {
      conditions.add(new ConfigSpecificLootCondition(configName));
    }
    conditions.add(InvertedLootItemCondition.invert(
        CanItemPerformAbility.canItemPerformAbility(ItemAbilities.SHEARS_DIG)
    ).build());
    return conditions.toArray(LootItemCondition[]::new);
  }


  @Override
  protected void start() {
    this.add("aubergine_from_short_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.SHORT_GRASS, 0.01f, "aubergine"), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_short_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.SHORT_GRASS, 0.008f, "grove_spores"), ModItems.GROVE_SPORES));
    this.add("wildroot_from_short_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.SHORT_GRASS, 0.004f, "wildroot"), ModItems.WILDROOT));

    this.add("aubergine_from_tall_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.TALL_GRASS, 0.01f, "aubergine"), ModItems.AUBERGINE_SEEDS));
    this.add("grove_spores_from_tall_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.TALL_GRASS, 0.008f, "grove_spores"), ModItems.GROVE_SPORES));
    this.add("wildroot_from_tall_grass", new AddGrassDropsModifier(getGrassConditions(RootsTags.Blocks.TALL_GRASS, 0.004f, "wildroot"), ModItems.WILDROOT));

    this.add("grove_spores_from_forageable_single_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS, 0.01f, false, null), ModItems.GROVE_SPORES));
    this.add("grove_spores_from_waterlogged_foreagable_single_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS, 0.03f, true, null), ModItems.GROVE_SPORES));
    this.add("grove_spores_from_foragable_double_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, 0.01f, false, null), ModItems.GROVE_SPORES));
    this.add("grove_spores_from_waterlogged_foreagable_double_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, 0.03f, true, null), ModItems.GROVE_SPORES));

    this.add("wildroot_from_forageable_single_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_SINGLE_BLOCKS, 0.01f, false, null), ModItems.WILDROOT));
    this.add("wildroot_from_foragable_double_blocks", new AddGrassDropsModifier(getForagingConditions(RootsTags.Blocks.FORAGEABLE_DOUBLE_BLOCKS, 0.01f, false, null), ModItems.WILDROOT));

    this.add("squid_tentacles", new AddTableLootModifier(new LootItemCondition[]{LootItemEntityPropertyCondition.hasProperties(
        LootContext.EntityTarget.THIS,
        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(RootsTags.Entities.ADD_TENTACLE_LOOT))
    ).build()}, RootsAPI.TENTACLES));

    this.add("elemental_crop_extra_drops", new ElementalCropExtraDropsModifier(getCropExtraConditions()));
  }
}
