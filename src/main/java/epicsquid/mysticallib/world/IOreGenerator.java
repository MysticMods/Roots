package epicsquid.mysticallib.world;

import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.MinableFeature;
import net.minecraftforge.fml.common.IWorldGenerator;

public interface IOreGenerator extends IWorldGenerator {

  default void generateOre(@Nonnull IBlockState ore, @Nonnull World world, @Nonnull Random random, int x, int z, int minY, int maxY, int size,
      int numberToGenerate) {
    for (int i = 0; i < numberToGenerate; i++) {
      BlockPos pos = new BlockPos(x * 16 + random.nextInt(16), random.nextInt(maxY - minY) + minY, z * 16 + random.nextInt(16));
      MinableFeature generator = new MinableFeature(ore, size);
      generator.generate(world, random, pos);
    }
  }
}
