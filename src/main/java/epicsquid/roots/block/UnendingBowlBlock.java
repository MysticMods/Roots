package epicsquid.roots.block;

import net.minecraft.block.Block;
import net.minecraft.util.BlockRenderLayer;

import javax.annotation.Nonnull;

public class UnendingBowlBlock extends Block {
  public UnendingBowlBlock(Properties properties) {
    super(properties);
  }

/*  public UnendingBowlBlock(@Nonnull Material mat, @Nonnull SoundType type, float hardness, @Nonnull String name, @Nonnull Class<? extends TileEntity> teClass) {
    super(mat, type, hardness, name, teClass);
    this.setItemBlock(new UnendingBowlBlockItem(this)).setRegistryName(LibRegistry.getActiveModid(), name);
  }*/

  @Nonnull
  @Override
  public BlockRenderLayer getRenderLayer() {
    return BlockRenderLayer.TRANSLUCENT;
  }

  // TODO: Botania integration
}
