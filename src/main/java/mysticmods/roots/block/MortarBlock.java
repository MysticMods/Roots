package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.block.entity.MortarBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class MortarBlock extends UseDelegatedBlock {
  public MortarBlock(Properties builder) {
    super(builder);
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
}
