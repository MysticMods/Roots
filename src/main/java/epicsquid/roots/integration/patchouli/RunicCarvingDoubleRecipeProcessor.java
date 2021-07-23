package epicsquid.roots.integration.patchouli;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.util.RitualUtil;
import net.minecraft.item.ItemStack;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

public class RunicCarvingDoubleRecipeProcessor implements IComponentProcessor {

  private RitualUtil.RunedWoodType recipe1 = null;
  private RitualUtil.RunedWoodType recipe2 = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe1");
    for (RitualUtil.RunedWoodType type : RitualUtil.RunedWoodType.values()) {
      if (type.toString().equalsIgnoreCase(recipeName)) {
        recipe1 = type;
      }
    }
    if (recipe1 == null) {
      throw new IllegalStateException("Not a valid recipe name: " + recipeName);
    }
    recipeName = iVariableProvider.get("recipe2");
    for (RitualUtil.RunedWoodType type : RitualUtil.RunedWoodType.values()) {
      if (type.toString().equalsIgnoreCase(recipeName)) {
        recipe2 = type;
      }
    }
    if (recipe2 == null) {
      throw new IllegalStateException("Not a valid recipe name: " + recipeName);
    }
  }

  @Override
  public String process(String s) {
    switch (s) {
      case "block1":
        return ItemStackUtil.serializeStack(recipe1.getVisual());
      case "rune1":
        return ItemStackUtil.serializeStack(new ItemStack(recipe1.getTopper()));
      case "block2":
        return ItemStackUtil.serializeStack(recipe2.getVisual());
      case "rune2":
        return ItemStackUtil.serializeStack(new ItemStack(recipe2.getTopper()));
      case "herb1":
      case "herb2":
        return ItemStackUtil.serializeStack(new ItemStack(ModItems.wildroot));
    }
    return null;
  }

}
