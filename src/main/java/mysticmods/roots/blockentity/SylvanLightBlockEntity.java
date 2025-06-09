package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SylvanLightBlockEntity extends BlockEntity implements ClientTickBlockEntity {
  private final RandomSource random;

  private static final int[][] COLORS = {
      {0xffe383, 0xffbd83},
      {0xffb4eb, 0x9da2ff},
      {0x9dfff9, 0xadff9d},
      {0xe7ff9d, 0x9db9ff},
      {0xffb69d, 0xff9dc4},
      {0x9dffa6, 0xc1ddff}
  };
  private int ticks;

  public SylvanLightBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.SYLVAN_LIGHT.get(), pos, blockState);
    this.random = RandomSource.create();
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    this.ticks++;

    Vec3 center = Vec3.atCenterOf(pPos);

    if (ticks % 3 == 0) {
/*
      pLevel.addParticle(RootsParticleOptions.builder(ModParticles.LARGE_LIGHT).color(0xffe383).build(),
          center.x,
          center.y,
          center.z,
          0,
          0,
          0);*/
    }

    if (ticks % 2 == 0) {
      if (random.nextInt(3) == 0) {
        return;
      }
      int[] color = COLORS[random.nextInt(COLORS.length)];
      Vec3 spot = Vec3.atCenterOf(pPos)
          .add((random.nextDouble() - 0.5) * 0.058, (random.nextDouble() - 0.5) * 0.02, (random.nextDouble() - 0.5) * 0.058);

      pLevel.addParticle(
          RootsParticleOptions.builder(
              ModParticles.LIGHT).color(
              color[0],
              color[1]).build(),
          spot.x, spot.y, spot.z,
          0,
          random.nextFloat() * 0.003,
          0
      );
    }
  }
}
