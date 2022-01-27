package mysticmods.roots.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import noobanidus.libs.noobutil.crafting.ICrafting;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

// TODO: Move to NoobUtil
public interface IIngredientStackRecipe<C extends ICrafting<?, ?>> extends IRecipe<C>, IProcessorRecipe<C> {
  NonNullList<IngredientStack> getIngredientStacks();

  @Override
  default ItemStack assemble(C pInv) {
    ItemStack resultCopy = getResultItem();
    for (Processor<C> processor : getProcessors()) {
      resultCopy = processor.processOutput(resultCopy, getIngredientStacks(), null, pInv);
    }
    return resultCopy;
  }

  @Override
  default NonNullList<Ingredient> getIngredients() {
    return NonNullList.create();
  }
}
