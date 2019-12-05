package epicsquid.roots.integration.patchouli;

public class MossRecipeProcessor { // implements IComponentProcessor {
/*  private static Ingredient INPUTS = Ingredient.EMPTY;
  private static Ingredient OUTPUTS = Ingredient.EMPTY;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
  }

  @Override
  public String process(String s) {
    if (s.startsWith("input")) {
      if (INPUTS == Ingredient.EMPTY) {
        INPUTS = Ingredient.fromStacks(MossConfig.getMossyCobblestones().keySet().toArray(new ItemStack[0]));
      }

      return ItemStackUtil.serializeIngredient(INPUTS);
    }

    if (s.startsWith("output")) {
      if (OUTPUTS == Ingredient.EMPTY) {
        OUTPUTS = Ingredient.fromStacks(MossConfig.getMossyCobblestones().values().toArray(new ItemStack[0]));
      }

      return ItemStackUtil.serializeIngredient(OUTPUTS);
    }

    if (s.equalsIgnoreCase("moss")) {
      return ItemStackUtil.serializeStack(MossRecipe.getTerraMoss());
    }

    return null;
  }*/
}
