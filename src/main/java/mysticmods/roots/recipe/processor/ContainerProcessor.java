package mysticmods.roots.recipe.processor;

import net.minecraft.item.ItemStack;
import noobanidus.libs.noobutil.crafting.ICrafting;
import noobanidus.libs.noobutil.ingredient.IngredientStack;
import noobanidus.libs.noobutil.processor.Processor;

import java.util.ArrayList;
import java.util.List;

public class ContainerProcessor<C extends ICrafting<?, ?>> extends Processor<C> {

  @Override
  public List<ItemStack> processIngredient(ItemStack result, IngredientStack ingredient, ItemStack usedItem, C crafter) {
    List<ItemStack> resultList = new ArrayList<>();
    if (usedItem.hasContainerItem()) {
      for (int i = 0; i < usedItem.getCount(); i++) {
        resultList.add(usedItem.getContainerItem());
      }
    }
    return resultList;
  }
}
