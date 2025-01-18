package mysticmods.roots.blockentity;

import mysticmods.roots.blockentity.inventory.LimitedItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

public class LimitedPedestalBlockEntity extends PedestalBlockEntity{
  public LimitedPedestalBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
    inventory = new LimitedItemStackHandler(1, 1) {
      @Override
      protected void onContentsChanged(int slot) {
        if (LimitedPedestalBlockEntity.this.hasLevel() && !LimitedPedestalBlockEntity.this.getLevel().isClientSide()) {
          LimitedPedestalBlockEntity.this.setChanged();
          Level level = LimitedPedestalBlockEntity.this.getLevel();
          BlockPos pos = LimitedPedestalBlockEntity.this.getBlockPos();
          BlockState state = LimitedPedestalBlockEntity.this.getBlockState();
          level.sendBlockUpdated(pos, state, state, 8);
          level.invalidateCapabilities(pos);
        }
      }
    };
  }
}
