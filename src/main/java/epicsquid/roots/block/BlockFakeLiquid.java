package epicsquid.roots.block;

import epicsquid.mysticallib.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

public class BlockFakeLiquid extends Block {
  public BlockFakeLiquid(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name) {
    super(mat, type, hardness, name);
    setItemBlock(null);
  }

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }
}
