package epicsquid.roots.integration.patchouli;

public class RitualRecipeProcessor { // implements IComponentProcessor {

/*
  private List<Ingredient> ingredients = new ArrayList<>();
  private ItemStack icon;

  @Override
  public void setup(IVariableProvider<String> iVariableProvider) {
    String ritualName = iVariableProvider.get("ritual");
    RitualBase ritualBase = RitualRegistry.ritualRegistry.get(ritualName);
    if (ritualBase == null) return;
    ingredients = ritualBase.getIngredients();
    icon = new ItemStack(ritualBase.getIcon());
  }

  @Override
  public String process(String s) {
    if (icon == null || ingredients.isEmpty()) return null;

    if (s.startsWith("item")) {
      int index = Integer.parseInt(s.substring(4)) - 1;
      Ingredient ingredient = ingredients.get(index);

      return ItemStackUtil.serializeIngredient(ingredient);
    } else if (s.equals("icon")) {
      return ItemStackUtil.serializeStack(icon);
    }

    return null;
  }
*/

}
