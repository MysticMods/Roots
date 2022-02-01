package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.processors.RootsProcessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

public interface IRootsRecipe <H extends IItemHandler, W extends IRootsCrafting<H>> extends IBoundlessRecipe<W> {
  @Override
  default ItemStack assemble(W pInv) {
    ItemStack resultCopy = getResultItem();
/*    for (RootsProcessor<? super W> processor : getProcessors()) {
      resultCopy = processor.processOutput(resultCopy, getIngredients(), null, pInv);
    }*/
    return resultCopy;
  }
}
