package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.init.ModSounds;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DecorativePyreBlock extends Block {
  public DecorativePyreBlock(Properties builder) {
    super(builder);
    registerDefaultState(defaultBlockState().setValue(PyreBlock.LIT, true).setValue(PyreBlock.BURNING, true));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(PyreBlock.LIT, PyreBlock.BURNING);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.PYRE;
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {
    double x = pPos.getX() + 0.5f;
    double y = pPos.getY() + 0.5f;
    double z = pPos.getZ() + 0.5f;
    if (pRandom.nextDouble() < 0.1) {
      pLevel.playLocalSound(x, y, z, ModSounds.PYRE_CRACKLES.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
    }

    int color1 = pState.is(ModBlocks.DECORATIVE_SOUL_PYRE) ? 0x5be3e8 : 0xc96c03;
    if (pRandom.nextBoolean()) {
      color1 = pState.is(ModBlocks.DECORATIVE_SOUL_PYRE) ? 0x9adfe1 : 0xe9bd39;
    }

    int color2 = pState.is(ModBlocks.DECORATIVE_SOUL_PYRE) ? 0x088f92 : 0x8b3100;

    if (pRandom.nextInt(4) == 0) {
      pLevel.addParticle(
          new ColorGravityParticleOptions(
              ModParticles.PYRE,
              color1,
              color2
          ),
          x + (pRandom.nextFloat() - 0.5f) * 0.3f,
          y + 0.1f + (pRandom.nextFloat()) * 0.2f,
          z + (pRandom.nextFloat() - 0.5f) * 0.3f,
          0,
          0,
          0
      );
    }
  }
}
