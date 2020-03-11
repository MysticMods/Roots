package epicsquid.roots.integration.jei.carving;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.RitualUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RunedWoodWrapper implements IRecipeWrapper {

  public final RitualUtil.RunedWoodType type;

  public RunedWoodWrapper(RitualUtil.RunedWoodType type) {
    this.type = type;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> inputs = new ArrayList<>();
    inputs.add(type.getVisual());
    inputs.add(new ItemStack(ModItems.wildroot));
    ingredients.setInputs(VanillaTypes.ITEM, inputs);
    ingredients.setOutput(VanillaTypes.ITEM, new ItemStack(type.getTopper()));
  }
}
