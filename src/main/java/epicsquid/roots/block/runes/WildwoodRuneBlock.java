package epicsquid.roots.block.runes;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WildwoodRuneBlock extends Block {


  public WildwoodRuneBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return null;
    /*    return new TileEntityWildrootRune();*/
  }

  // TODO: @Override
  //public Item getItemDropped(BlockState state, Random rand, int fortune) {
  //  return Item.getItemFromBlock(ModBlocks.wildwood_log);
  //}
}
