package epicsquid.roots.util;

import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class RitualUtil {

    private static Random rand = new Random();

    public static BlockPos getRandomPosRadial(BlockPos centerPos, int xRadius, int zRadius)
    {
        BlockPos pos = centerPos.add(-xRadius, 0, -zRadius);

        pos = pos.add(rand.nextInt(xRadius*2), 0, rand.nextInt(zRadius*2));

        System.out.println("Pos: " +  pos.getX() +  " - " + pos.getY() + " - " + pos.getZ());

        return pos;
    }

}
