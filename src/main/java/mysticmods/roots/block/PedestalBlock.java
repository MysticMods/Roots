package mysticmods.roots.block;

import mysticmods.roots.api.InventoryBlockEntity;
import mysticmods.roots.block.entity.PedestalBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public abstract class PedestalBlock extends UseDelegatedBlock {
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public PedestalBlock(Properties p_49795_) {
    super(p_49795_);
    registerDefaultState(defaultBlockState().setValue(VALID, false));
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

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
    super.createBlockStateDefinition(pBuilder);
    pBuilder.add(VALID);
  }
}
