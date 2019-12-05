package epicsquid.roots.integration.patchouli;

public class FeyCraftingRecipeProcessor {/* implements IComponentProcessor {

  private FeyCraftingRecipe groveCraftingRecipe = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String recipeName = iVariableProvider.get("recipe");
    groveCraftingRecipe = ModRecipes.getFeyCraftingRecipe(recipeName);
  }

  @Override
  public String process(String s) {
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;

      if (index >= groveCraftingRecipe.getIngredients().size()) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      Ingredient ingredient = groveCraftingRecipe.getIngredients().get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    }

    if (s.equalsIgnoreCase("result")) {
      return ItemStackUtil.serializeStack(groveCraftingRecipe.getResult());
    }
    return null;
  }*/
}
