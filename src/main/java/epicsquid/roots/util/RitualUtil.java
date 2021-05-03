package epicsquid.roots.util;

import com.google.common.collect.Sets;
import epicsquid.mysticallib.util.Util;
import epicsquid.roots.config.RunedWoodConfig;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityCatalystPlate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RitualUtil {

  private static Random rand = Util.rand;

  public static BlockPos getRandomPosRadialXZ(BlockPos centerPos, int xRadius, int zRadius) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() - xRadius, centerPos.getY(), centerPos.getZ() - zRadius);

    return pos.add(rand.nextInt(xRadius * 2), 0, rand.nextInt(zRadius * 2)); // TODO: Not immutable?
  }

  public static BlockPos getRandomGroundPosition(BlockPos center, int xRadius, int zRadius) {
    int randX = rand.nextInt(xRadius * 2) - xRadius;
    int randZ = rand.nextInt(zRadius * 2) - zRadius;
    return center.add(randX, 0, randZ);
  }

  public static BlockPos getRandomPosRadialXYZ(BlockPos centerPos, int xRadius, int yRadius, int zRadius) {
    BlockPos pos = new BlockPos(centerPos.getX() - xRadius, centerPos.getY() - yRadius, centerPos.getZ() - zRadius);

    pos = pos.add(rand.nextInt(xRadius * 2), rand.nextInt(yRadius * 2), rand.nextInt(zRadius * 2));

    return pos;
  }

  @Nullable
  public static BlockPos getRandomPosRadialXYZ(World world, BlockPos centerPos, int xRadius, int yRadius, int zRadius, Block... whitelistedBlocks) {
    BlockPos pos = new BlockPos(centerPos.getX() - xRadius, centerPos.getY() - yRadius, centerPos.getZ() - zRadius);

    Set<Block> blocks = Sets.newHashSet(whitelistedBlocks);

    for (int i = 0; i < xRadius * yRadius * zRadius; i++) {
      pos = pos.add(
          xRadius > 0 ? rand.nextInt(xRadius * 2) : 0,
          yRadius > 0 ? rand.nextInt(yRadius * 2) : 0,
          zRadius > 0 ? rand.nextInt(zRadius * 2) : 0);

      IBlockState state = world.getBlockState(pos);

      if (blocks.contains(state.getBlock())) {
        return pos;
      }
    }

    return null;
  }

  /**
   * Get a random integer between min and max inclusive.
   */
  public static int getRandomInteger(int min, int max) {
    return rand.nextInt(max - min + 1) + min;
  }

  /**
   * Checks if the given block has water adjacent to it
   *
   * @return True if at least one side is touching a water source block
   */
  public static boolean isAdjacentToWater(World world, BlockPos pos) {
    for (EnumFacing facing : EnumFacing.HORIZONTALS) {
      Block block = world.getBlockState(pos.offset(facing)).getBlock();
      if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
        return true;
      }
    }
    return false;
  }

  public static AxisAlignedBB CATALYST = new AxisAlignedBB(-6, -6, -6, 7, 7, 7);

  public static List<TileEntityCatalystPlate> getNearbyCatalystPlates(World world, BlockPos pos) {
    AxisAlignedBB bounds = CATALYST.offset(pos);
    BlockPos max = max(bounds);
    BlockPos min = min(bounds);

    List<TileEntityCatalystPlate> result = new ArrayList<>();

    for (BlockPos p : BlockPos.getAllInBoxMutable(max, min)) {
      if (world.isAirBlock(p)) {
        continue;
      }

      IBlockState state = world.getBlockState(p);
      if (state.getBlock() == ModBlocks.catalyst_plate || state.getBlock() == ModBlocks.reinforced_catalyst_plate) {
        TileEntity te = world.getTileEntity(p);
        if (te instanceof TileEntityCatalystPlate) {
          result.add((TileEntityCatalystPlate) te);
        }
      }
    }

    return result;
  }

  public static List<ItemStack> getItemsFromNearbyPlates(List<TileEntityCatalystPlate> plates) {
    List<ItemStack> stacks = new ArrayList<>();
    for (TileEntityCatalystPlate plate : plates) {
      ItemStack stack = plate.getHeldItem();
      if (!stack.isEmpty()) {
        stacks.add(stack);
      }
    }
    return stacks;
  }

  public static AxisAlignedBB RADIUS = new AxisAlignedBB(-9, -9, -9, 10, 10, 10);

  public static int getNearbyStandingStones(World world, BlockPos pos, int height) {
    return getNearbyPositions(Runestone.get(), world, pos, height).size();
  }

  public static int getNearbyPillar(StandingPillar pillar, World world, BlockPos pos, int height) {
    return getNearbyPositions(pillar, world, pos, height).size();
  }

  public static List<BlockPos> getNearbyPositions(StandingPillar pillar, World world, BlockPos pos, int height) {
    List<BlockPos> positions = new ArrayList<>();

    AxisAlignedBB bounds = RADIUS.offset(pos);
    BlockPos max = max(bounds);
    BlockPos min = min(bounds);

    for (BlockPos p : BlockPos.getAllInBoxMutable(max, min)) {
      IBlockState state = world.getBlockState(p);
      if (pillar.matchesTop(state)) {
        BlockPos start = p.toImmutable().down();
        IBlockState startState;
        int column = 1;

        while (start.getY() > (p.getY() - 10)) {
          startState = world.getBlockState(start);
          if (!pillar.matchesBase(startState)) {
            break;
          }

          start = start.down();
          column++;
        }

        if (column == height || (height == -1 && column >= 3)) {
          positions.add(p.toImmutable());
        }
      }
    }

    return positions;
  }

  public static BlockPos min(AxisAlignedBB box) {
    return new BlockPos(box.minX, box.minY, box.minZ);
  }

  public static BlockPos max(AxisAlignedBB box) {
    return new BlockPos(box.maxX, box.maxY, box.maxZ);
  }

  public interface StandingPillar {
    boolean matchesBase(IBlockState state);

    boolean matchesTop(IBlockState state);
  }

  public static class Runestone implements StandingPillar {
    private static Runestone INSTANCE = null;
    private static Set<Block> toppers = new HashSet<>();
    private static Set<Block> bases = new HashSet<>();

    public Runestone() {
      if (toppers.isEmpty()) {
        toppers.add(ModBlocks.chiseled_runestone);
        toppers.add(ModBlocks.chiseled_runed_obsidian);
      }
      if (bases.isEmpty()) {
        bases.add(ModBlocks.runestone);
        bases.add(ModBlocks.runed_obsidian);
      }
    }

    public static Runestone get() {
      if (INSTANCE == null) {
        INSTANCE = new Runestone();
      }
      return INSTANCE;
    }

    @Override
    public boolean matchesBase(IBlockState state) {
      return bases.contains(state.getBlock());
    }

    @Override
    public boolean matchesTop(IBlockState state) {
      return toppers.contains(state.getBlock());
    }
  }

  public enum RunedWoodType implements StandingPillar {
    ACACIA(RunedWoodConfig.ACACIA),
    OAK(RunedWoodConfig.OAK),
    DARK_OAK(RunedWoodConfig.DARK_OAK),
    BIRCH(RunedWoodConfig.BIRCH),
    JUNGLE(RunedWoodConfig.JUNGLE),
    SPRUCE(RunedWoodConfig.SPRUCE),
    WILDWOOD(RunedWoodConfig.WILDWOOD);

    private final RunedWoodConfig.RunedPillarConfig config;

    RunedWoodType(RunedWoodConfig.RunedPillarConfig config) {
      this.config = config;
    }

    public ItemStack getVisual() {
      return this.config.getItemStack();
    }

    public IBlockState getBase() {
      return this.config.getPillarState();
    }

    public Block getTopper() {
      return this.config.getCapstoneState().getBlock();
    }

    @Override
    public boolean matchesBase(IBlockState state) {
      return this.config.getPillarMatcher().test(state);
    }

    @Override
    public boolean matchesTop(IBlockState state) {
      return this.config.getCapstoneMatcher().test(state);
    }

    @Nullable
    public static IBlockState matchesAny(IBlockState state) {
      for (RunedWoodType type : RunedWoodType.values()) {
        if (type.matchesBase(state)) {
          return type.getTopper().getDefaultState();
        }
      }
      return null;
    }
  }
}
