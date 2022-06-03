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

public class GrovePedestalBlock extends PedestalBlock {
  public GrovePedestalBlock(Properties p_49795_) {
    super(p_49795_);
  }

  @Override
  public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
    return Shapes.GROVE_PEDESTAL;
  }

}
