package epicsquid.roots.integration.jei.mortar;

public class MortarWrapper { //implements IRecipeWrapper {

/*  public MortarRecipe recipe;
  public SpellBase spellBase;

  public MortarWrapper(MortarRecipe recipe) {
    this.recipe = recipe;
  }

  public MortarWrapper(SpellBase spellBase) {
    this.spellBase = spellBase;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<List<ItemStack>> inputs = new ArrayList<>();

    if (recipe != null) {
      for (Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == null || ingredient == Ingredient.EMPTY) {
          inputs.add(new ArrayList<>());
        } else {
          inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
        }
      }
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getResult());
    } else if (spellBase != null) {
      for (Ingredient ingredient : spellBase.getIngredients()) {
        inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
      }
      ingredients.setOutput(VanillaTypes.ITEM, spellBase.getResult());
    }
    ingredients.setInputLists(VanillaTypes.ITEM, inputs);
  }*/
}
