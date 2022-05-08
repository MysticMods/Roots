package mysticmods.roots.block;

import mysticmods.roots.api.reference.Shapes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import noobanidus.libs.noobutil.block.BaseBlocks;
import noobanidus.libs.noobutil.util.VoxelUtil;

public class CatalystPlateBlock extends BaseBlocks.HorizontalBlock {
  public static VoxelShape SOUTH = Shapes.CATALYST_PLATE;
  public static VoxelShape NORTH = VoxelUtil.rotateHorizontal(SOUTH, Direction.SOUTH);
  public static VoxelShape WEST = VoxelUtil.rotateHorizontal(SOUTH, Direction.EAST);
  public static VoxelShape EAST = VoxelUtil.rotateHorizontal(SOUTH, Direction.WEST);

  public CatalystPlateBlock(Properties builder) {
    super(builder);
  }
/*
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CatalystPlateBlockEntity(ModBlockEntities.CATALYST_PLATE.get());
    }*/

  @Override
  public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
    switch (p_220053_1_.getValue(FACING)) {
      case NORTH:
        return NORTH;
      default:
      case SOUTH:
        return SOUTH;
      case EAST:
        return EAST;
      case WEST:
        return WEST;
    }
  }
}
