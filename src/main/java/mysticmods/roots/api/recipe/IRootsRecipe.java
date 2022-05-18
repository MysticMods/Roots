package mysticmods.roots.api.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public interface IRootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> extends IBoundlessRecipe<W> {
  default NonNullList<ItemStack> process (List<ItemStack> ingredients) {
    NonNullList<ItemStack> result = NonNullList.create();
    for (ItemStack stack : ingredients) {
      if (stack.hasContainerItem()) {
        result.add(stack.getContainerItem());
      }
    }
    return result;
  }
}
