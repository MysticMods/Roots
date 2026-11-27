package mysticmods.roots.api.recipe.output;

import net.minecraft.world.item.ItemStack;

public interface IOutput {
  default ItemStack output () {
    return ItemStack.EMPTY;
  }

  default float chance () {
    return 1f;
  }
}
