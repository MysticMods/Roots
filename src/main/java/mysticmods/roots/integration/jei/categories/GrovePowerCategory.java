package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.GroveNumber;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.block.SimpleBlockType;
import mysticmods.roots.integration.jei.widget.SymmetryWidget;
import mysticmods.roots.recipe.fake.GrovePowerRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public class GrovePowerCategory implements IRecipeCategory<GrovePowerRecipe> {
  private final IDrawable background;
  private final IDrawable icon;
  private final Component title;

  private GrovePowerCategory(IGuiHelper helper, int width, int height, ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    this.background = helper.createDrawable(background, 0, 0, width, height);
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, icon.get());
    this.title = title;
  }

  public GrovePowerCategory(IGuiHelper helper) {
    this(helper, 116, 21, RootsAPI.rl("textures/gui/jei/grove_power_entry.png"), () -> new ItemStack(ModItems.WILD_GROVE_STONE.get()), Component.translatable("roots.jei.grove_power"));
  }


  @Override
  public RecipeType<GrovePowerRecipe> getRecipeType() {
    return RootsJEIPlugin.GROVE_POWER_RECIPE_TYPE;
  }

  @Override
  public Component getTitle() {
    return title;
  }

  @SuppressWarnings("removal")
  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, GrovePowerRecipe recipe, IFocusGroup iFocusGroup) {
    builder.addSlot(RecipeIngredientRole.INPUT, 2, 2)
        .addIngredients(RootsJEIPlugin.BLOCK_TYPE, SimpleBlockType.fromTag(recipe.blockTag()))
        .setCustomRenderer(RootsJEIPlugin.BLOCK_TYPE, RootsJEIPlugin.BLOCK_RENDERER);
    builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addIngredients(recipe.itemIngredient());
    builder.addSlot(RecipeIngredientRole.CATALYST, 61, 2)
        .addIngredients(Ingredient.of(RootsTags.Groves.getGroveStoneTag(recipe.groveTag())));
    var power = GroveNumber.power(recipe.groveTag(), recipe.power());
    builder.addSlot(RecipeIngredientRole.OUTPUT, 99, 2)
        .addIngredient(RootsJEIPlugin.GROVE_NUMBER_TYPE, power)
        .setCustomRenderer(RootsJEIPlugin.GROVE_NUMBER_TYPE, RootsJEIPlugin.GROVE_NUMBER_RENDERER);
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, GrovePowerRecipe recipe, IFocusGroup focuses) {
    IRecipeCategory.super.createRecipeExtras(builder, recipe, focuses);

    builder.addWidget(new SymmetryWidget(23, 2, recipe.symmetry()));

  }
}
