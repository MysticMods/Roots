package mysticmods.roots.block;

import mysticmods.roots.blockentity.AmplifierBlockEntity;
import mysticmods.roots.blockentity.EnchantedTurfBlockEntity;
import mysticmods.roots.blockentity.template.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnchantedTurfBlock extends GrassBlock implements EntityBlock {
  public EnchantedTurfBlock(Properties p_53685_) {
    super(p_53685_);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new EnchantedTurfBlockEntity(pPos, pState);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    if (!level.isClientSide()) {
      return BaseBlockEntity::serverTick;
    } else {
      return BaseBlockEntity::clientTick;
    }
  }
}
