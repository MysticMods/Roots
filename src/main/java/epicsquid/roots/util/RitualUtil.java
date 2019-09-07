package epicsquid.roots.util;

import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RitualUtil {

  private static Random rand = new Random();

    public static BlockPos getRandomPosRadialXZ(BlockPos centerPos, int xRadius, int zRadius)
    {
        BlockPos pos = centerPos.add(-xRadius, 0, -zRadius);

        pos = pos.add(rand.nextInt(xRadius*2), 0, rand.nextInt(zRadius*2));

        //System.out.println("Pos: " +  pos.getX() +  " | " + pos.getY() + " | " + pos.getZ());
        return pos;
    }

    public static BlockPos getRandomPosRadialXYZ(BlockPos centerPos, int xRadius, int yRadius, int zRadius)
    {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(centerPos.getX() -xRadius, centerPos.getY() -yRadius, centerPos.getZ() -zRadius);

        BlockPos pos2 = pos.add(rand.nextInt(xRadius * 2), rand.nextInt(yRadius * 2), rand.nextInt(zRadius * 2));
        //System.out.println("Pos: " +  pos.getX() +  " | " + pos.getY() + " | " + pos.getZ());
        return pos2;
    }

}
