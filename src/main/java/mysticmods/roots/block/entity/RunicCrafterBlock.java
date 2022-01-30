package mysticmods.roots.block.entity;

import mysticmods.roots.api.reference.Shapes;
import mysticmods.roots.block.FeyCrafterBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class RunicCrafterBlock extends FeyCrafterBlock {
  public RunicCrafterBlock(Properties builder) {
    super(builder);
  }

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
    return Shapes.RUNIC_CRAFTER;
  }
}
