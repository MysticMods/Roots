package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.block.FeyLightBlock;
import mysticmods.roots.init.ModBlockEntities;
import mysticmods.roots.init.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
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

  }
}
