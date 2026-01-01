package mysticmods.roots.item.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class EnchantedTurfBlockItem extends BlockItem {
  public EnchantedTurfBlockItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }
}
