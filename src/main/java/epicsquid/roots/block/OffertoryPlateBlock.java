package epicsquid.roots.block;

import net.minecraft.block.Block;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.properties.BlockStateProperties;

@SuppressWarnings("deprecation")
public class OffertoryPlateBlock extends Block {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

  public OffertoryPlateBlock(Properties properties) {
    super(properties);
  }

  // TODO: FACING
  // TODO: Voxel shape
}
