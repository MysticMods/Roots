package mysticmods.roots.api.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public interface IRootsRecipe<H extends IItemHandler, W extends IRootsCrafting<H>> extends IBoundlessRecipe<W> {
  @Override
  default ItemStack assemble(W pInv) {
    ItemStack resultCopy = getResultItem();
/*    for (RootsProcessor<? super W> processor : getProcessors()) {
      resultCopy = processor.processOutput(resultCopy, getIngredients(), null, pInv);
    }*/
    return resultCopy;
  }
}
