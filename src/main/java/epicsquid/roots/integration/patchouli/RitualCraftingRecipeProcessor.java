package epicsquid.roots.integration.patchouli;

public class RitualCraftingRecipeProcessor { //implements IComponentProcessor {
/*
  private PyreCraftingRecipe pyreCraftingRecipe = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String ritualName = iVariableProvider.get("recipe");
    pyreCraftingRecipe = ModRecipes.getCraftingRecipe(ritualName);
  }

  @Override
  public String process(String s) {
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;

      if (index >= pyreCraftingRecipe.getIngredients().size()) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      Ingredient ingredient = pyreCraftingRecipe.getIngredients().get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    }

    if (s.equalsIgnoreCase("result")) {
      return ItemStackUtil.serializeStack(pyreCraftingRecipe.getResult());
    }
    return null;
  }*/

}
