package epicsquid.roots.block.runes;

import epicsquid.roots.tileentity.TileEntityWildrootRune;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class OvergrowthRuneBlock extends Block {

  public OvergrowthRuneBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new TileEntityWildrootRune();
  }
}
