package mysticmods.roots.block;

import mysticmods.roots.api.block.StateProperties;
import mysticmods.roots.api.blockentity.InventoryBlockEntity;
import mysticmods.roots.api.property.Property;
import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.blockentity.MortarBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MortarBlock extends UseDelegatedBlock {
  public static final BooleanProperty PESTLE_INSIDE = StateProperties.Mortar.PESTLE_INSIDE;

  public MortarBlock(Properties builder) {
    super(builder);
    this.registerDefaultState(this.getStateDefinition().any().setValue(PESTLE_INSIDE, false));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(PESTLE_INSIDE);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.MORTAR;
  }

  @org.jetbrains.annotations.Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new MortarBlockEntity(ModBlockEntities.MORTAR.get(), pPos, pState);
  }

  @Override
  public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
    if (pState.hasBlockEntity() && (!pState.is(pNewState.getBlock()) || !pNewState.hasBlockEntity()) && pLevel.getBlockEntity(pPos) instanceof InventoryBlockEntity ibe) {
      Containers.dropContents(pLevel, pPos, ibe.getItems());
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
