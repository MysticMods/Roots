package mysticmods.roots.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CreativeComponentPouch extends Item {
  public CreativeComponentPouch(Properties properties) {
    super(properties);
  }

  @Override
  public boolean isFoil(ItemStack stack) {
    return true;
  }
}
