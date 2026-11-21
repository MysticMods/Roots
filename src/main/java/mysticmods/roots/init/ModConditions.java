package mysticmods.roots.init;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.grove.Rank;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.condition.*;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class ModConditions {
  private static final DeferredRegister<MapCodec<? extends ICondition>> TYPES = DeferredRegister.create(RootsRegistries.Keys.CONDITION_TYPES, RootsAPI.MODID);

  public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<GroveStoneCondition>> GROVE_STONE_CONDITION_TYPE = TYPES.register("grove_stone_condition", () -> GroveStoneCondition.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<PillarCondition>> PILLAR_CONDITION_TYPE = TYPES.register("pillar_condition", () -> PillarCondition.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<FluidSourcePropertyCondition>> FLUID_SOURCE_CONDITION_TYPE = TYPES.register("fluid_source_property_condition", () -> FluidSourcePropertyCondition.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<BlockStatePropertyCondition>> BLOCK_STATE_CONDITION_TYPE = TYPES.register("block_state_property_condition", () -> BlockStatePropertyCondition.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<OvergrowthCondition>> OVERGROWTH_CONDITION_TYPE = TYPES.register("overgrowth_condition", () -> OvergrowthCondition.MAP_CODEC);
  public static final DeferredHolder<MapCodec<? extends ICondition>, MapCodec<GroveRankReputation>> GROVE_RANK_CONDITION_TYPE = TYPES.register("grove_rank_condition", () -> GroveRankReputation.MAP_CODEC);

  public static final ResourceKey<ICondition> TALL_RUNESTONE_PILLAR = key("tall_runestone");
  public static final ResourceKey<ICondition> SHORT_RUNESTONE_PILLAR = key("short_runestone");
  public static final ResourceKey<ICondition> TALL_RUNED_OBSIDIAN_PILLAR = key("tall_runed_obsidian");
  public static final ResourceKey<ICondition> SHORT_RUNED_OBSIDIAN_PILLAR = key("short_runed_obsidian");
  public static final ResourceKey<ICondition> SHORT_PILLAR = key("short_pillar");
  public static final ResourceKey<ICondition> TALL_PILLAR = key("tall_pillar");
  public static final ResourceKey<ICondition> ANY_PILLAR = key("any_pillar");

  public static final ResourceKey<ICondition> ANY_GROVE_STONE_ACTIVE = key("active_grove_stone");
  public static final ResourceKey<ICondition> UNITY_GROVE_STONE_ACTIVE = key("active_unity_grove_stone");
  public static final ResourceKey<ICondition> FUNGAL_GROVE_STONE_ACTIVE = key("active_fungal_grove_stone");
  public static final ResourceKey<ICondition> ANY_GROVE_STONE_INACTIVE = key("inactive_grove_stone");
  public static final ResourceKey<ICondition> ANY_GROVE_STONE = key("any_grove_stone");

  public static final ResourceKey<ICondition> MATURE_WILDROOT_CROP = key("mature_wildroot_crop");
  public static final ResourceKey<ICondition> OVERGROWTH = key("overgrowth");

  public static final ResourceKey<ICondition> FUNGAL_RANK_1 = rankKey("fungal", Rank.FIRST);
  public static final ResourceKey<ICondition> ELEMENTAL_RANK_1 = rankKey("elemental", Rank.FIRST);
  public static final ResourceKey<ICondition> UNITY_RANK_1 = rankKey("unity", Rank.FIRST);
  public static final ResourceKey<ICondition> ANIMATE_RANK_1 = rankKey("animate", Rank.FIRST);
  public static final ResourceKey<ICondition> CULTIVATION_RANK_1 = rankKey("cultivation", Rank.FIRST);
  public static final ResourceKey<ICondition> TWILIGHT_RANK_1 = rankKey("twilight", Rank.FIRST);

  public static final ResourceKey<ICondition> FUNGAL_RANK_2 = rankKey("fungal", Rank.SECOND);
  public static final ResourceKey<ICondition> ELEMENTAL_RANK_2 = rankKey("elemental", Rank.SECOND);
  public static final ResourceKey<ICondition> UNITY_RANK_2 = rankKey("unity", Rank.SECOND);
  public static final ResourceKey<ICondition> ANIMATE_RANK_2 = rankKey("animate", Rank.SECOND);
  public static final ResourceKey<ICondition> CULTIVATION_RANK_2 = rankKey("cultivation", Rank.SECOND);
  public static final ResourceKey<ICondition> TWILIGHT_RANK_2 = rankKey("twilight", Rank.SECOND);

  public static final ResourceKey<ICondition> FUNGAL_RANK_3 = rankKey("fungal", Rank.THIRD);
  public static final ResourceKey<ICondition> ELEMENTAL_RANK_3 = rankKey("elemental", Rank.THIRD);
  public static final ResourceKey<ICondition> UNITY_RANK_3 = rankKey("unity", Rank.THIRD);
  public static final ResourceKey<ICondition> ANIMATE_RANK_3 = rankKey("animate", Rank.THIRD);
  public static final ResourceKey<ICondition> CULTIVATION_RANK_3 = rankKey("cultivation", Rank.THIRD);
  public static final ResourceKey<ICondition> TWILIGHT_RANK_3 = rankKey("twilight", Rank.THIRD);

  public static final ResourceKey<ICondition> FUNGAL_RANK_4 = rankKey("fungal", Rank.FOURTH);
  public static final ResourceKey<ICondition> ELEMENTAL_RANK_4 = rankKey("elemental", Rank.FOURTH);
  public static final ResourceKey<ICondition> UNITY_RANK_4 = rankKey("unity", Rank.FOURTH);
  public static final ResourceKey<ICondition> ANIMATE_RANK_4 = rankKey("animate", Rank.FOURTH);
  public static final ResourceKey<ICondition> CULTIVATION_RANK_4 = rankKey("cultivation", Rank.FOURTH);
  public static final ResourceKey<ICondition> TWILIGHT_RANK_4 = rankKey("twilight", Rank.FOURTH);

  public static void register(IEventBus bus) {
    TYPES.register(bus);
  }

  private static ResourceKey<ICondition> key(String name) {
    return ResourceKey.create(RootsRegistries.Keys.CONDITIONS, RootsAPI.rl(name));
  }

  // Same naming as the old GroveRankReputation.getName(), so lang keys carry over.
  private static ResourceKey<ICondition> rankKey(String grove, Rank rank) {
    return key(grove + "_reputation_rank_" + rank.getSerializedName());
  }
}
