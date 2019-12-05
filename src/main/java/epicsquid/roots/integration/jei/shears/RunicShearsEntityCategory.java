package epicsquid.roots.integration.jei.shears;

public class RunicShearsEntityCategory { // implements IRecipeCategory<RunicShearsEntityWrapper> {

/*  private final IDrawable background;

  public RunicShearsEntityCategory(IGuiHelper helper) {
    this.background = helper.createDrawable(new ResourceLocation(Roots.MODID, "textures/gui/jei/runic_shears_entity.png"), 0, 0, 122, 84);
  }

  @Override
  public String getUid() {
    return JEIRootsPlugin.RUNIC_SHEARS_ENTITY;
  }

  @Override
  public String getTitle() {
    return I18n.format("container." + JEIRootsPlugin.RUNIC_SHEARS_ENTITY + ".name");
  }

  @Override
  public String getModName() {
    return Roots.NAME;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public void setRecipe(IRecipeLayout recipeLayout, RunicShearsEntityWrapper recipeWrapper, IIngredients ingredients) {
    IGuiItemStackGroup group = recipeLayout.getItemStacks();
    group.addTooltipCallback((slotIndex, input, ingredient, tooltip) -> {
      tooltip.add("");
      tooltip.add(I18n.format("jei.roots.runic_shears.cooldown", recipeWrapper.getCooldown()));
    });
    group.init(0, true, 104, 32);
    group.set(0, ingredients.getOutputs(VanillaTypes.ITEM).get(0));
  }*/
}
