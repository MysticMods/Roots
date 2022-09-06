package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.condition.LevelCondition;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModConditions {
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_4_HIGH = REGISTRATE.simple("rune_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.runePillar(4)));
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_3_HIGH = REGISTRATE.simple("rune_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.runePillar(3)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_4_HIGH = REGISTRATE.simple("acacia_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_3_HIGH = REGISTRATE.simple("acacia_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_4_HIGH = REGISTRATE.simple("birch_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_3_HIGH = REGISTRATE.simple("birch_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_4_HIGH = REGISTRATE.simple("crimson_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_3_HIGH = REGISTRATE.simple("crimson_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_4_HIGH = REGISTRATE.simple("dark_oak_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_3_HIGH = REGISTRATE.simple("dark_oak_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_4_HIGH = REGISTRATE.simple("jungle_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_3_HIGH = REGISTRATE.simple("jungle_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_4_HIGH = REGISTRATE.simple("oak_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_3_HIGH = REGISTRATE.simple("oak_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_4_HIGH = REGISTRATE.simple("spruce_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_3_HIGH = REGISTRATE.simple("spruce_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_4_HIGH = REGISTRATE.simple("warped_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_3_HIGH = REGISTRATE.simple("warped_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_4_HIGH = REGISTRATE.simple("wildwood_pillar_4_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_3_HIGH = REGISTRATE.simple("wildwood_pillar_3_high", LevelCondition.class, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3)));

  public static void load () {
  }
}
