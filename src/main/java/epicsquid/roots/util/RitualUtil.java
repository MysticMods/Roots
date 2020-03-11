package epicsquid.roots.util;

import com.google.common.collect.Sets;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityOfferingPlate;
import net.minecraft.block.*;
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

  private static Random rand = new Random();

  public static BlockPos getRandomPosRadialXZ(BlockPos centerPos, int xRadius, int zRadius) {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() - xRadius, centerPos.getY(), centerPos.getZ() - zRadius);

    return pos.add(rand.nextInt(xRadius * 2), 0, rand.nextInt(zRadius * 2));
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

  public static AxisAlignedBB OFFERING = new AxisAlignedBB(-6, -6, -6, 7, 7, 7);

  public static List<TileEntityOfferingPlate> getNearbyOfferingPlates(World world, BlockPos pos) {
    AxisAlignedBB bounds = OFFERING.offset(pos);
    BlockPos max = max(bounds);
    BlockPos min = min(bounds);

    List<TileEntityOfferingPlate> result = new ArrayList<>();

    for (BlockPos p : BlockPos.getAllInBoxMutable(max, min)) {
      if (world.isAirBlock(p)) {
        continue;
      }

      IBlockState state = world.getBlockState(p);
      if (state.getBlock() == ModBlocks.offering_plate || state.getBlock() == ModBlocks.reinforced_offering_plate) {
        TileEntity te = world.getTileEntity(p);
        if (te instanceof TileEntityOfferingPlate) {
          result.add((TileEntityOfferingPlate) te);
        }
      }
    }

    return result;
  }

  public static List<ItemStack> getItemsFromNearbyPlates(List<TileEntityOfferingPlate> plates) {
    List<ItemStack> stacks = new ArrayList<>();
    for (TileEntityOfferingPlate plate : plates) {
      ItemStack stack = plate.getHeldItem();
      if (!stack.isEmpty()) {
        stacks.add(stack);
      }
    }
    return stacks;
  }

  public static AxisAlignedBB RADIUS = new AxisAlignedBB(-9, -9, -9, 10, 10, 10);

  public static int getNearbyStandingStones (World world, BlockPos pos, int height) {
    return getNearbyPositions(Runestone.get(), world, pos, height).size();
  }

  public static List<BlockPos> getNearbyPositions(StandingPillar pillar,  World world, BlockPos pos, int height) {
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
    boolean matchesBase (IBlockState state);
    boolean matchesTop (IBlockState state);
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

    public static Runestone get () {
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
    ACACIA(() -> ModBlocks.runed_acacia, (o) -> o.getBlock() == Blocks.LOG2 && o.getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.ACACIA, new ItemStack(Blocks.LOG2, 1, 0)),
    OAK(() -> ModBlocks.runed_oak, (o) -> o.getBlock() == Blocks.LOG && o.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.OAK, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.OAK.getMetadata())),
    DARK_OAK(() -> ModBlocks.runed_dark_oak, (o) -> o.getBlock() == Blocks.LOG2 && o.getValue(BlockNewLog.VARIANT) == BlockPlanks.EnumType.DARK_OAK, new ItemStack(Blocks.LOG2, 1, 1)),
    BIRCH(() -> ModBlocks.runed_birch, (o) -> o.getBlock() == Blocks.LOG && o.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.BIRCH, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.BIRCH.getMetadata())),
    JUNGLE(() -> ModBlocks.runed_jungle, (o) -> o.getBlock() == Blocks.LOG && o.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.JUNGLE.getMetadata())),
    SPRUCE(() -> ModBlocks.runed_spruce, (o) -> o.getBlock() == Blocks.LOG && o.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.SPRUCE, new ItemStack(Blocks.LOG, 1, BlockPlanks.EnumType.SPRUCE.getMetadata())),
    WILDWOOD(() -> ModBlocks.runed_wildwood, (o) -> o.getBlock() == ModBlocks.wildwood_log, new ItemStack(ModBlocks.wildwood_log));

    private Supplier<Block> supplier;
    private Predicate<IBlockState> matcher;
    private ItemStack visual;

    RunedWoodType(Supplier<Block> supplier, Predicate<IBlockState> matcher, ItemStack visual) {
      this.supplier = supplier;
      this.matcher = matcher;
      this.visual = visual;
    }

    public ItemStack getVisual() {
      return visual;
    }

    public Block getTopper () {
      return supplier.get();
    }

    @Override
    public boolean matchesBase(IBlockState state) {
      return matcher.test(state);
    }

    @Override
    public boolean matchesTop(IBlockState state) {
      return state.getBlock() == getTopper();
    }

    @Nullable
    public static IBlockState matchesAny (IBlockState state) {
      for (RunedWoodType type : RunedWoodType.values()) {
        if (type.matchesBase(state)) {
          return type.getTopper().getDefaultState();
        }
      }
      return null;
    }
  }
}
