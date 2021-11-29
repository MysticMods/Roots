package mysticmods.roots.blocks;

import net.minecraft.block.HorizontalBlock;
import net.minecraft.state.DirectionProperty;

public class CatalystPlateBlock extends HorizontalBlock {
  public static final DirectionProperty FACING = HorizontalBlock.FACING;

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
}
