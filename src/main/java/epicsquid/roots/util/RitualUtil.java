package epicsquid.roots.util;

import com.google.common.collect.Sets;
import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

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

  public static AxisAlignedBB OFFERING = new AxisAlignedBB(-6, -6, -6, 7, 7, 7);

  public static List<TileEntityOffertoryPlate> getNearbyOfferingPlates(World world, BlockPos pos) {
    AxisAlignedBB bounds = OFFERING.offset(pos);
    BlockPos max = max(bounds);
    BlockPos min = min(bounds);

    List<TileEntityOffertoryPlate> result = new ArrayList<>();

    for (BlockPos p : BlockPos.getAllInBoxMutable(max, min)) {
      if (world.isAirBlock(p)) {
        continue;
      }

      IBlockState state = world.getBlockState(p);
      if (state.getBlock() == ModBlocks.offertory_plate) {
        TileEntity te = world.getTileEntity(p);
        if (te instanceof TileEntityOffertoryPlate) {
          result.add((TileEntityOffertoryPlate) te);
        }
      }
    }

    return result;
  }

  public static List<ItemStack> getItemsFromNearbyPlates(List<TileEntityOffertoryPlate> plates) {
    List<ItemStack> stacks = new ArrayList<>();
    for (TileEntityOffertoryPlate plate : plates) {
      ItemStack stack = plate.getHeldItem();
      if (!stack.isEmpty()) {
        stacks.add(stack);
      }
    }
    return stacks;
  }

  public static AxisAlignedBB STONES = new AxisAlignedBB(-9, -9, -9, 10, 10, 10);

  public static int getNearbyStandingStones (World world, BlockPos pos, int height) {
    return getNearbyStandingStonePositions(world, pos, height).size();
  }

  public static List<BlockPos> getNearbyStandingStonePositions (World world, BlockPos pos, int height) {
    List<BlockPos> positions = new ArrayList<>();
    Set<Block> toppers = Sets.newHashSet(ModBlocks.chiseled_runestone, ModBlocks.chiseled_runed_obsidian);
    Set<Block> basis = Sets.newHashSet(ModBlocks.runestone, ModBlocks.runed_obsidian);

    AxisAlignedBB bounds = STONES.offset(pos);
    BlockPos max = max(bounds);
    BlockPos min = min(bounds);

    int count = 0;
    for (BlockPos p : BlockPos.getAllInBoxMutable(max, min)) {
      IBlockState state = world.getBlockState(p);
      if (toppers.contains(state.getBlock())) {
        BlockPos start = p.toImmutable().down();
        IBlockState startState;
        int column = 1;

        while (start.getY() > (p.getY() - 10)) {
          startState = world.getBlockState(start);
          if (!basis.contains(startState.getBlock())) {
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

}
