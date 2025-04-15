package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class SylvanLightBlockEntity extends BlockEntity implements ClientTickBlockEntity {
  private int ticks;

  public SylvanLightBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.SYLVAN_LIGHT.get(), pos, blockState);
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {

  }
}
