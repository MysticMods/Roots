package epicsquid.roots.api;

import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

public class RitualRecipeProcessor implements IComponentProcessor {

  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack icon;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String ritualName = iVariableProvider.get("ritual");
    RitualBase ritualBase = RitualRegistry.ritualRegistry.get(ritualName);
    ingredients = ritualBase.getIngredients();
    icon = new ItemStack(ritualBase.getIcon());
  }

  @Override
  public String process(String s) {
    if(s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      Ingredient ingredient = ingredients.get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    } else if (s.equals("icon")) {
      return ItemStackUtil.serializeStack(icon);
    }

    return null;
  }

}
