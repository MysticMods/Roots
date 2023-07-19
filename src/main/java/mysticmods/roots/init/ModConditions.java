package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.faction.GroveType;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.test.block.BlockPropertyMatchTest;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModConditions {
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_4_HIGH = REGISTRATE.simple("rune_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.runePillar(4)));
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_3_HIGH = REGISTRATE.simple("rune_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.runePillar(3)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_4_HIGH = REGISTRATE.simple("acacia_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_3_HIGH = REGISTRATE.simple("acacia_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_4_HIGH = REGISTRATE.simple("birch_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_3_HIGH = REGISTRATE.simple("birch_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_4_HIGH = REGISTRATE.simple("crimson_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_3_HIGH = REGISTRATE.simple("crimson_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_4_HIGH = REGISTRATE.simple("dark_oak_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_3_HIGH = REGISTRATE.simple("dark_oak_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_4_HIGH = REGISTRATE.simple("jungle_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_3_HIGH = REGISTRATE.simple("jungle_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_4_HIGH = REGISTRATE.simple("oak_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_3_HIGH = REGISTRATE.simple("oak_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_4_HIGH = REGISTRATE.simple("spruce_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_3_HIGH = REGISTRATE.simple("spruce_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_4_HIGH = REGISTRATE.simple("warped_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_3_HIGH = REGISTRATE.simple("warped_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_4_HIGH = REGISTRATE.simple("wildwood_pillar_4_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_3_HIGH = REGISTRATE.simple("wildwood_pillar_3_high", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3)));
  public static final RegistryEntry<LevelCondition> PRIMAL_GROVE_STONE_ANY = REGISTRATE.simple("primal_grove_stone_any", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.groveStone(GroveType.PRIMAL, false)));
  public static final RegistryEntry<LevelCondition> PRIMAL_GROVE_STONE_VALID = REGISTRATE.simple("primal_grove_stone_valid", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.groveStone(GroveType.PRIMAL, true)));
  public static final RegistryEntry<LevelCondition> GROVE_STONE_ANY = REGISTRATE.simple("grove_stone_any", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.anyGroveStone(false)));
  public static final RegistryEntry<LevelCondition> GROVE_STONE_VALID = REGISTRATE.simple("grove_stone_valid", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.anyGroveStone(true)));
  public static final RegistryEntry<LevelCondition> MATURE_WILDROOT_CROP = REGISTRATE.simple("mature_wildroot_crop", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(new LevelCondition.BlockStatePropertyCondition(new BlockPropertyMatchTest(ModBlocks.WILDROOT_CROP.getDefaultState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE))));

  public static void load() {
  }
}
