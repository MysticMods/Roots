package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.faction.GroveType;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.test.block.BlockPropertyMatchTest;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


public class ModConditions {
  private static final DeferredRegister<LevelCondition> REGISTER = DeferredRegister.create(RootsRegistries.LEVEL_CONDITIONS, RootsAPI.MODID);

  public static final DeferredHolder<LevelCondition, LevelCondition> RUNE_PILLAR_4_HIGH = REGISTER.register("4_high_rune_pillar", () -> LevelCondition.runePillar(4));
  public static final DeferredHolder<LevelCondition, LevelCondition> RUNE_PILLAR_3_HIGH = REGISTER.register("3_high_rune_pillar", () -> LevelCondition.runePillar(3));
  public static final DeferredHolder<LevelCondition, LevelCondition> LOG_PILLAR_4_HIGH = REGISTER.register("4_high_log_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ANY_LOG, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> LOG_PILLAR_3_HIGH = REGISTER.register("3_high_log_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ANY_LOG, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> ACACIA_PILLAR_4_HIGH = REGISTER.register("4_high_acacia_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> ACACIA_PILLAR_3_HIGH = REGISTER.register("3_high_acacia_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> BIRCH_PILLAR_4_HIGH = REGISTER.register("4_high_birch_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> BIRCH_PILLAR_3_HIGH = REGISTER.register("3_high_birch_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> CRIMSON_PILLAR_4_HIGH = REGISTER.register("4_high_crimson_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> CRIMSON_PILLAR_3_HIGH = REGISTER.register("3_high_crimson_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> DARK_OAK_PILLAR_4_HIGH = REGISTER.register("4_high_dark_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> DARK_OAK_PILLAR_3_HIGH = REGISTER.register("3_high_dark_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> JUNGLE_PILLAR_4_HIGH = REGISTER.register("4_high_jungle_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> JUNGLE_PILLAR_3_HIGH = REGISTER.register("3_high_jungle_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> OAK_PILLAR_4_HIGH = REGISTER.register("4_high_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> OAK_PILLAR_3_HIGH = REGISTER.register("3_high_oak_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> SPRUCE_PILLAR_4_HIGH = REGISTER.register("4_high_spruce_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> SPRUCE_PILLAR_3_HIGH = REGISTER.register("3_high_spruce_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> WARPED_PILLAR_4_HIGH = REGISTER.register("4_high_warped_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> WARPED_PILLAR_3_HIGH = REGISTER.register("3_high_warped_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> WILDWOOD_PILLAR_4_HIGH = REGISTER.register("4_high_wildwood_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4));
  public static final DeferredHolder<LevelCondition, LevelCondition> WILDWOOD_PILLAR_3_HIGH = REGISTER.register("3_high_wildwood_pillar", () -> LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3));
  public static final DeferredHolder<LevelCondition, LevelCondition> PRIMAL_GROVE_STONE_ANY = REGISTER.register("any_primal_grove_stone", () -> LevelCondition.groveStone(GroveType.PRIMAL, false));
  public static final DeferredHolder<LevelCondition, LevelCondition> PRIMAL_GROVE_STONE_VALID = REGISTER.register("valid_primal_grove_stone", () -> LevelCondition.groveStone(GroveType.PRIMAL, true));
  public static final DeferredHolder<LevelCondition, LevelCondition> GROVE_STONE_ANY = REGISTER.register("any_grove_stone", () -> LevelCondition.anyGroveStone(false));
  public static final DeferredHolder<LevelCondition, LevelCondition> GROVE_STONE_VALID = REGISTER.register("valid_grove_stone", () -> LevelCondition.anyGroveStone(true));
  public static final DeferredHolder<LevelCondition, LevelCondition> MATURE_WILDROOT_CROP = REGISTER.register("mature_wildroot_crop", () -> new LevelCondition.BlockStatePropertyCondition(new BlockPropertyMatchTest(ModBlocks.WILDROOT_CROP.get().defaultBlockState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE)));

/*  private static Supplier<List<BlockState>> groveStone (GroveType type, boolean valid) {
    return () -> {
      List<BlockState> result = new ArrayList<>();
      if (type == GroveType.PRIMAL) {
        result.add(ModBlocks.PRIMAL_GROVE_STONE.get().defaultBlockState().setValue(GroveStoneBlock.PART, StateProperties.Part.BOTTOM).setValue(GroveStoneBlock.FACING, Direction.NORTH).setValue(GroveStoneBlock.VALID, valid));
        result.add(ModBlocks.PRIMAL_GROVE_STONE.get().defaultBlockState().setValue(GroveStoneBlock.PART, StateProperties.Part.MIDDLE).setValue(GroveStoneBlock.FACING, Direction.NORTH).setValue(GroveStoneBlock.VALID, valid));
        result.add(ModBlocks.PRIMAL_GROVE_STONE.get().defaultBlockState().setValue(GroveStoneBlock.PART, StateProperties.Part.TOP).setValue(GroveStoneBlock.FACING, Direction.NORTH).setValue(GroveStoneBlock.VALID, valid));
      } else {
        throw new NotImplementedException("groveStone not implemented for type " + type);
      }

      return result;
    };
  }*/

  public static void register(IEventBus bus) {
    REGISTER.register(bus);
  }
}
