package mysticmods.roots.blockentity;

import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class VisibleBlockEntity extends BlockEntity {
  public VisibleBlockEntity(BlockPos pos, BlockState blockState) {
    super(ModBlockEntities.VISIBLE.get(), pos, blockState);
  }
}
