package epicsquid.roots.integration.jei.spell;

public class SpellModifierWrapper { //implements IRecipeWrapper {

/*  public final SpellBase recipe;
  public final List<ItemStack> module_items;

  public SpellModifierWrapper(SpellBase recipe) {
    this.recipe = recipe;
    this.module_items = recipe.getModuleStacks();
  }

  @Override
  public void getIngredients(IIngredients ingredients) {
    ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    ingredients.setInputs(VanillaTypes.ITEM, module_items);
  }

  @Override
  public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
    String spell_name = I18n.format("roots.spell." + recipe.getName() + ".name");
    String name = recipe.getTextColor() + "" + TextFormatting.BOLD + spell_name + TextFormatting.RESET;
    int x = (75 - minecraft.fontRenderer.getStringWidth(spell_name)) / 2;
    minecraft.fontRenderer.drawString(name, x, 3, Color.BLACK.getRGB());
  }*/
}
