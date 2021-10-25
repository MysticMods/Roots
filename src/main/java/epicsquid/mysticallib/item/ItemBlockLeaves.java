package epicsquid.mysticallib.item;

import epicsquid.mysticallib.block.BlockLeavesBase;
import net.minecraft.item.BlockItem;

public class ItemBlockLeaves extends BlockItem {
  public ItemBlockLeaves(BlockLeavesBase block) {
    super(block);
    this.setMaxDamage(0);
  }

  @Override
  public int getMetadata(int damage) {
    return damage | 4;
  }
}
