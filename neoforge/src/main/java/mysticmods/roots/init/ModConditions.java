package mysticmods.roots.init;

import com.tterrag.registrate.util.entry.RegistryEntry;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.StateProperties;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.faction.GroveType;
import mysticmods.roots.block.GroveStoneBlock;
import mysticmods.roots.block.crop.ThreeStageCropBlock;
import mysticmods.roots.test.block.BlockPropertyMatchTest;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import noobanidus.libs.noobutil.type.LazySupplier;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static mysticmods.roots.Roots.REGISTRATE;

public class ModConditions {
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_4_HIGH = REGISTRATE.simple("4_high_rune_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.runePillar(4), pillar(LevelCondition.PillarType.ANY_RUNE, 4)));
  public static final RegistryEntry<LevelCondition> RUNE_PILLAR_3_HIGH = REGISTRATE.simple("3_high_rune_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.runePillar(3), pillar(LevelCondition.PillarType.ANY_RUNE, 3)));
  public static final RegistryEntry<LevelCondition> LOG_PILLAR_4_HIGH = REGISTRATE.simple("4_high_log_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ANY_LOG, 4), pillar(LevelCondition.PillarType.ANY_LOG, 4)));
  public static final RegistryEntry<LevelCondition> LOG_PILLAR_3_HIGH = REGISTRATE.simple("3_high_log_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ANY_LOG, 3), pillar(LevelCondition.PillarType.ANY_LOG, 3)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_4_HIGH = REGISTRATE.simple("4_high_acacia_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 4), pillar(LevelCondition.PillarType.ACACIA, 4)));
  public static final RegistryEntry<LevelCondition> ACACIA_PILLAR_3_HIGH = REGISTRATE.simple("3_high_acacia_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.ACACIA, 3), pillar(LevelCondition.PillarType.ACACIA, 3)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_4_HIGH = REGISTRATE.simple("4_high_birch_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 4), pillar(LevelCondition.PillarType.BIRCH, 4)));
  public static final RegistryEntry<LevelCondition> BIRCH_PILLAR_3_HIGH = REGISTRATE.simple("3_high_birch_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.BIRCH, 3), pillar(LevelCondition.PillarType.BIRCH, 3)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_4_HIGH = REGISTRATE.simple("4_high_crimson_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 4), pillar(LevelCondition.PillarType.CRIMSON, 4)));
  public static final RegistryEntry<LevelCondition> CRIMSON_PILLAR_3_HIGH = REGISTRATE.simple("3_high_crimson_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.CRIMSON, 3), pillar(LevelCondition.PillarType.CRIMSON, 3)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_4_HIGH = REGISTRATE.simple("4_high_dark_oak_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 4), pillar(LevelCondition.PillarType.DARK_OAK, 4)));
  public static final RegistryEntry<LevelCondition> DARK_OAK_PILLAR_3_HIGH = REGISTRATE.simple("3_high_dark_oak_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.DARK_OAK, 3), pillar(LevelCondition.PillarType.DARK_OAK, 3)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_4_HIGH = REGISTRATE.simple("4_high_jungle_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 4), pillar(LevelCondition.PillarType.JUNGLE, 4)));
  public static final RegistryEntry<LevelCondition> JUNGLE_PILLAR_3_HIGH = REGISTRATE.simple("3_high_jungle_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.JUNGLE, 3), pillar(LevelCondition.PillarType.JUNGLE, 3)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_4_HIGH = REGISTRATE.simple("4_high_oak_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 4), pillar(LevelCondition.PillarType.OAK, 4)));
  public static final RegistryEntry<LevelCondition> OAK_PILLAR_3_HIGH = REGISTRATE.simple("3_high_oak_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.OAK, 3), pillar(LevelCondition.PillarType.OAK, 3)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_4_HIGH = REGISTRATE.simple("4_high_spruce_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 4), pillar(LevelCondition.PillarType.SPRUCE, 4)));
  public static final RegistryEntry<LevelCondition> SPRUCE_PILLAR_3_HIGH = REGISTRATE.simple("3_high_spruce_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.SPRUCE, 3), pillar(LevelCondition.PillarType.SPRUCE, 3)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_4_HIGH = REGISTRATE.simple("4_high_warped_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 4), pillar(LevelCondition.PillarType.WARPED, 4)));
  public static final RegistryEntry<LevelCondition> WARPED_PILLAR_3_HIGH = REGISTRATE.simple("3_high_warped_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WARPED, 3), pillar(LevelCondition.PillarType.WARPED, 3)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_4_HIGH = REGISTRATE.simple("4_high_wildwood_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 4), pillar(LevelCondition.PillarType.WILDWOOD, 4)));
  public static final RegistryEntry<LevelCondition> WILDWOOD_PILLAR_3_HIGH = REGISTRATE.simple("3_high_wildwood_pillar", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.logPillar(LevelCondition.PillarType.WILDWOOD, 3), pillar(LevelCondition.PillarType.WILDWOOD, 3)));
  public static final RegistryEntry<LevelCondition> PRIMAL_GROVE_STONE_ANY = REGISTRATE.simple("any_primal_grove_stone", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.groveStone(GroveType.PRIMAL, false), groveStone(GroveType.PRIMAL, false)));
  public static final RegistryEntry<LevelCondition> PRIMAL_GROVE_STONE_VALID = REGISTRATE.simple("valid_primal_grove_stone", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.groveStone(GroveType.PRIMAL, true), groveStone(GroveType.PRIMAL, true)));
  public static final RegistryEntry<LevelCondition> GROVE_STONE_ANY = REGISTRATE.simple("any_grove_stone", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.anyGroveStone(false), groveStone(GroveType.PRIMAL, false)));
  public static final RegistryEntry<LevelCondition> GROVE_STONE_VALID = REGISTRATE.simple("valid_grove_stone", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(LevelCondition.anyGroveStone(true), groveStone(GroveType.PRIMAL, true)));
  public static final RegistryEntry<LevelCondition> MATURE_WILDROOT_CROP = REGISTRATE.simple("mature_wildroot_crop", RootsAPI.LEVEL_CONDITION_REGISTRY, () -> new LevelCondition(new LevelCondition.BlockStatePropertyCondition(new BlockPropertyMatchTest(ModBlocks.WILDROOT_CROP.getDefaultState().setValue(ThreeStageCropBlock.AGE, 3), ThreeStageCropBlock.AGE)), canonical(ModBlocks.WILDROOT_CROP.getDefaultState().setValue(ThreeStageCropBlock.AGE, 3))));

  private static Supplier<List<BlockState>> canonical (Block... blocks) {
    final List<BlockState> result = Arrays.stream(blocks).map(Block::defaultBlockState).collect(Collectors.toList());
    return () -> result;
  }

  private static Supplier<List<BlockState>> canonical (BlockState ... blocks) {
    final List<BlockState> result = Arrays.stream(blocks).collect(Collectors.toList());
    return () -> result;
  }

  private static Supplier<List<BlockState>> pillar (LevelCondition.PillarType type, int height) {
    return () -> {
      BlockState pillar = ForgeRegistries.BLOCKS.tags().getTag(type.getPillarTag()).stream().findFirst().orElseThrow(() -> new IllegalStateException("No block found for pillar type " + type)).defaultBlockState();
      BlockState capstone = ForgeRegistries.BLOCKS.tags().getTag(type.getCapstoneTag()).stream().findFirst().orElseThrow(() -> new IllegalStateException("No block found for pillar type " + type)).defaultBlockState();
      List<BlockState> result = new ArrayList<>();
      for (int i = 0; i < height - 1; i++) {
        result.add(pillar);
      }
      result.add(capstone);
      return result;
    };
  }

  private static Supplier<List<BlockState>> groveStone (GroveType type, boolean valid) {
    return () -> {
      List<BlockState> result = new ArrayList<>();
      if (type == GroveType.PRIMAL) {
        result.add(ModBlocks.PRIMAL_GROVE_STONE.getDefaultState().setValue(GroveStoneBlock.PART, StateProperties.Part.BOTTOM).setValue(GroveStoneBlock.FACING, Direction.NORTH).setValue(GroveStoneBlock.VALID, valid));
        result.add(ModBlocks.PRIMAL_GROVE_STONE.getDefaultState().setValue(GroveStoneBlock.PART, StateProperties.Part.MIDDLE).setValue(GroveStoneBlock.FACING, Direction.NORTH).setValue(GroveStoneBlock.VALID, valid));
        result.add(ModBlocks.PRIMAL_GROVE_STONE.getDefaultState().setValue(GroveStoneBlock.PART, StateProperties.Part.TOP).setValue(GroveStoneBlock.FACING, Direction.NORTH).setValue(GroveStoneBlock.VALID, valid));
      } else {
        throw new NotImplementedException("groveStone not implemented for type " + type);
      }

      return result;
    };
  }

  public static void load() {
  }
}
