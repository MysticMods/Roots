package epicsquid.mysticallib.block;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;

public interface IBlock {

  @Nullable
  Item getItemBlock();

  BlockItem setItemBlock (BlockItem block);
}
