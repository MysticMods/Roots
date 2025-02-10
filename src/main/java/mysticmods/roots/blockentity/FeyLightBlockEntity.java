package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.block.FeyLightBlock;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.SimpleParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FeyLightBlockEntity extends BlockEntity implements ClientTickBlockEntity {
  private int ticks;

  public FeyLightBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.FEY_LIGHT.get(), pos, blockState);
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {
    ticks++;
    if (ticks % 40 != 0 || ticks == 1) {
      return;
    }
    RandomSource pRandom = pLevel.getRandom();
    DyeColor color = null;

    if (pState.getValue(FeyLightBlock.COLORED)) {
      color = pState.getValue(FeyLightBlock.COLOR);
    }

    int r, g, b;

    /*    if (color == null) {*/
    int index = pRandom.nextInt(5);
    r = FeyLightBlock.UNCOLORED[0][index];
    g = FeyLightBlock.UNCOLORED[1][index];
    b = FeyLightBlock.UNCOLORED[2][index];
    /*    }*/

    int color1 = r << 16 | g << 8 | b;

    pLevel.addParticle(
        new SimpleParticleOptions(
            ModParticles.FEY_LIGHT,
            color1,
            0.0f
        ),
        pPos.getX() + 0.5,
        pPos.getY() + 0.5,
        pPos.getZ() + 0.5,
        0,
        0,
        0
    );
  }
}
