package epicsquid.roots.integration.jei.fey;

public class FeyWrapper { // implements IRecipeWrapper {

/*  public FeyCraftingRecipe recipe;

  public FeyWrapper(FeyCraftingRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    if (recipe != null) {
      List<List<ItemStack>> inputs = new ArrayList<>();
      for (Ingredient ingredient : recipe.getIngredients()) {
        if (ingredient == null || ingredient == Ingredient.EMPTY) {
          inputs.add(new ArrayList<>());
        } else {
          inputs.add(Arrays.asList(ingredient.getMatchingStacks()));
        }
      }
      ingredients.setInputLists(VanillaTypes.ITEM, inputs);
      ingredients.setOutput(VanillaTypes.ITEM, this.recipe.getResult());
    }
  }*/
}
