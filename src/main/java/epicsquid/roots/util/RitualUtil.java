package epicsquid.roots.util;

import epicsquid.roots.init.ModBlocks;
import epicsquid.roots.tileentity.TileEntityOffertoryPlate;
import javafx.scene.chart.Axis;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RitualUtil {

  private static Random rand = new Random();

  public static BlockPos getRandomPosRadial(BlockPos centerPos, int xRadius, int zRadius) {
    BlockPos pos = centerPos.add(-xRadius, 0, -zRadius);

    pos = pos.add(rand.nextInt(xRadius * 2), 0, rand.nextInt(zRadius * 2));

    return pos;
  }

  public static AxisAlignedBB BOUNDING = new AxisAlignedBB(-6, -6, -6, 6, 6, 6);

  public static List<TileEntityOffertoryPlate> getNearbyOfferingPlates (World world, BlockPos pos) {
    AxisAlignedBB bounds = BOUNDING.offset(pos);
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

  public static List<ItemStack> getItemsFromNearbyPlates (List<TileEntityOffertoryPlate> plates) {
    List<ItemStack> stacks = new ArrayList<>();
    for (TileEntityOffertoryPlate plate : plates) {
      ItemStack stack = plate.getHeldItem();
      if (!stack.isEmpty()) {
        stacks.add(stack);
      }
    }
    return stacks;
  }

  public static BlockPos min (AxisAlignedBB box) {
    return new BlockPos(box.minX, box.minY, box.minZ);
  }

  public static BlockPos max (AxisAlignedBB box) {
    return new BlockPos(box.maxX, box.maxY, box.maxZ);
  }

}
