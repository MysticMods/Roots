package epicsquid.roots.integration.jei.spell;

public class SpellCostWrapper { //implements IRecipeWrapper {

/*  public final SpellBase recipe;

  public SpellCostWrapper(SpellBase recipe) {
    this.recipe = recipe;
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    List<ItemStack> costs = this.recipe.getCostItems();
    costs.add(this.recipe.getResult());
    ingredients.setInputs(VanillaTypes.ITEM, costs);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    int row = 0;
    String spell_name = I18n.format("roots.spell." + recipe.getName() + ".name");
    String name = recipe.getTextColor() + "" + TextFormatting.BOLD + spell_name + TextFormatting.RESET;
    int x = (110 - minecraft.fontRenderer.getStringWidth(spell_name)) / 2;
    minecraft.fontRenderer.drawString(name, x, 3, Color.BLACK.getRGB());

    for (double cost : recipe.getCosts().values()) {
      String c = String.format("x %.4f", cost);
      minecraft.fontRenderer.drawString(c, 82, row == 0 ? 20 : 40, Color.BLACK.getRGB());
      row++;
    }
  }*/
}
