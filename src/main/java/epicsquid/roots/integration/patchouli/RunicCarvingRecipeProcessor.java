package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class RunicCarvingRecipeProcessor implements IComponentProcessor {

  private RitualUtil.RunedWoodType recipe = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe");
    for (RitualUtil.RunedWoodType type : RitualUtil.RunedWoodType.values()) {
      if (type.toString().equalsIgnoreCase(recipeName)) {
        recipe = type;
      }
    }
  }

  @Override
  public String process(String s) {
    if (recipe == null) {
      return ItemStackUtil.serializeStack(ItemStack.EMPTY);
    }
    if (s.equals("block")) {
      return ItemStackUtil.serializeStack(recipe.getVisual());
    } else if (s.equals("rune")) {
      return ItemStackUtil.serializeStack(new ItemStack(recipe.getTopper()));
    } else if (s.equals("herb")) {
      return ItemStackUtil.serializeStack(new ItemStack(ModItems.wildroot));
    }
    return null;
  }

}
