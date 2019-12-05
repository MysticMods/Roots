package epicsquid.roots.block;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

public class FeyCrafterBlock extends Block {
  public FeyCrafterBlock(Properties properties) {
    super(properties);
  }

/*  public FeyCrafterBlock(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    setLightOpacity(0);
  }*/

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.CUTOUT;
  }

  // TODO: Voxel shape
}
