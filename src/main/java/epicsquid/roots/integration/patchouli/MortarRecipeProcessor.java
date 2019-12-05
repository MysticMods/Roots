package epicsquid.roots.integration.patchouli;

public class MortarRecipeProcessor { //implements IComponentProcessor {

/*  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack output = null;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String item = iVariableProvider.get("item");
    int meta = iVariableProvider.has("meta") ? Integer.parseInt(iVariableProvider.get("meta")) : 0;
    MortarRecipe mortarBase = ModRecipes.getMortarRecipe(item, meta);
    if (mortarBase != null) {
      ingredients = mortarBase.getIngredients();
      output = mortarBase.getResult();
    }
  }

  @Override
  public String process(String s) {
    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      Ingredient ingredient = ingredients.get(index);
      if (ingredient == null || ingredient == Ingredient.EMPTY) {
        return ItemStackUtil.serializeStack(ItemStack.EMPTY);
      }

      return ItemStackUtil.serializeIngredient(ingredient);
    }
    if (s.equalsIgnoreCase("result")) {
      return ItemStackUtil.serializeStack(output);
    }
    return null;
  }*/

}
