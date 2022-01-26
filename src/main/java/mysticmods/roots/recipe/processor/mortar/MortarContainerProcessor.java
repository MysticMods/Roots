package mysticmods.roots.recipe.processor.mortar;

import mysticmods.roots.recipe.MortarCrafting;
import net.minecraft.item.ItemStack;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import java.util.ArrayList;
import java.util.List;

public class MortarContainerProcessor extends MortarProcessor {
  public MortarContainerProcessor() {
  }

  @Override
  public List<ItemStack> processIngredient(ItemStack result, IngredientStack ingredient, ItemStack usedItem, MortarCrafting crafter) {
    List<ItemStack> resultList = new ArrayList<>();
    if (usedItem.hasContainerItem()) {
      for (int i = 0; i < usedItem.getCount(); i++) {
        resultList.add(usedItem.getContainerItem());
      }
    }
    return resultList;
  }
}
