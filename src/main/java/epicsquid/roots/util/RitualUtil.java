package epicsquid.roots.util;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RitualUtil {

  private static Random rand = new Random();

  public static BlockPos getRandomPosRadialXZ(BlockPos centerPos, int xRadius, int zRadius)
  {
    BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() -xRadius, centerPos.getY(), centerPos.getZ() -zRadius);

    return pos.add(rand.nextInt(xRadius * 2), 0, rand.nextInt(zRadius * 2));
  }

  public static BlockPos getRandomPosRadialXYZ(BlockPos centerPos, int xRadius, int yRadius, int zRadius)
  {
    BlockPos pos = new BlockPos(centerPos.getX() -xRadius, centerPos.getY() -yRadius, centerPos.getZ() -zRadius);

    pos = pos.add(rand.nextInt(xRadius * 2), rand.nextInt(yRadius * 2), rand.nextInt(zRadius * 2));

    //Debug Print
    //System.out.println("Pos: " +  pos.getX() +  " | " + pos.getY() + " | " + pos.getZ());
    return pos;
  }

  public static BlockPos getRandomPosRadialXYZ(World world, BlockPos centerPos, int xRadius, int yRadius, int zRadius, Block... whitelistedBlocks)
  {
    BlockPos pos = new BlockPos(centerPos.getX() -xRadius, centerPos.getY() -yRadius, centerPos.getZ() -zRadius);

    pos = pos.add(
            xRadius > 0 ? rand.nextInt(xRadius * 2) : 0,
            yRadius > 0 ? rand.nextInt(yRadius * 2) : 0,
            zRadius > 0 ? rand.nextInt(zRadius * 2) : 0);

    //System.out.println("Pos: " +  pos.getX() +  " | " + pos.getY() + " | " + pos.getZ());
    List<Block> blocks = Arrays.asList(whitelistedBlocks);

    if (blocks.contains(world.getBlockState(pos)))
      return pos;

    return null;
  }
}