package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.particleslib.client.particle.Particles;
import noobanidus.libs.particleslib.init.ModParticles;

import java.util.Random;

public class DecorativePyreBlock extends Block {
  public DecorativePyreBlock(Properties builder) {
    super(builder);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.PYRE;
  }

  @Override
  public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRandom) {
    super.animateTick(pState, pLevel, pPos, pRandom);

    Particles.create(ModParticles.FIERY_PARTICLE.get())
        .addVelocity(0.00525f * (pRandom.nextFloat() - 0.5f), 0, 0.00525f * (pRandom.nextFloat() - 0.5f))
        .setAlpha(0.5f, 0.2f)
        .setScale(0.8f + 0.5f * pRandom.nextFloat())
        .setColor(230 / 255.0f, 55 / 255.0f, 16 / 255.0f, 230 / 255.0f, 83 / 255.0f, 16 / 255.0f)
        .setLifetime(80)
        .disableGravity()
        .setSpin(0)
        .spawn(pLevel, pPos.getX() + 0.5f + 0.3f * (pRandom.nextFloat() - 0.5f), pPos.getY() + 0.625f + 0.125f * pRandom.nextFloat(), pPos.getZ() + 0.5f + 0.3f * (pRandom.nextFloat() - 0.5f));
  }
}
