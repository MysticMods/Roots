package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.block.entity.GroveCrafterBlockEntity;
import mysticmods.roots.block.entity.template.BaseBlockEntity;
import mysticmods.roots.block.entity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class GroveCrafterBlock extends UseDelegatedBlock implements EntityBlock {
  public GroveCrafterBlock(Properties builder) {
    super(builder);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.GROVE_CRAFTER;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
    return new GroveCrafterBlockEntity(ModBlockEntities.GROVE_CRAFTER.get(), pPos, pState);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
    if (pLevel.isClientSide()) {
      return BaseBlockEntity::clientTick;
    } else {
      return BaseBlockEntity::serverTick;
    }
  }
}
