package mysticmods.roots.block.entity;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.block.FeyCrafterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RunicCrafterBlock extends FeyCrafterBlock {
  public RunicCrafterBlock(Properties builder) {
    super(builder);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    return Shapes.RUNIC_CRAFTER;
  }
}
