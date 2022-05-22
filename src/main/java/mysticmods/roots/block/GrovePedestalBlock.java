package mysticmods.roots.block;

import mysticmods.roots.api.InventoryBlockEntity;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.block.entity.PedestalBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GrovePedestalBlock extends UseDelegatedBlock {
  public GrovePedestalBlock(Properties p_49795_) {
    super(p_49795_);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return Shapes.GROVE_PEDESTAL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new PedestalBlockEntity(ModBlockEntities.PEDESTAL.get(), pPos, pState);
  }

  @Override
  public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
    if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity()) && pLevel.getBlockEntity(pPos) instanceof InventoryBlockEntity ibe) {
      Containers.dropContents(pLevel, pPos, ibe.getItems());
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
