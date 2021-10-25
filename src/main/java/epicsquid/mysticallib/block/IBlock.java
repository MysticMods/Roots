package epicsquid.mysticallib.block;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public interface IBlock {

  @Nullable
  Item getItemBlock();

  ItemBlock setItemBlock (ItemBlock block);
}
