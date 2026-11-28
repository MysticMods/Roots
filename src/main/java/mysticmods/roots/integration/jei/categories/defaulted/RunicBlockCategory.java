package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.WorldRecipeUtil;
import mysticmods.roots.recipe.runic.RunicBlockRecipe;
import mysticmods.roots.util.Pos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class RunicBlockCategory extends RootsDefaultCategory<RunicBlockRecipe> {
  public RunicBlockCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.RUNIC_RECIPE_TYPE, helper, () -> new ItemStack(ModItems.RUNIC_SHEARS.get()), Component.translatable("roots.jei.runic_block"), RootsAPI.rl("textures/gui/jei/default_block.png"));
  }

  @Override
  protected void addInputs(IRecipeLayoutBuilder builder, RunicBlockRecipe recipe, IFocusGroup iFocusGroup, Pos pos) {
    super.addInputs(builder, recipe, iFocusGroup, pos);

    WorldRecipeUtil.setWorldRecipe(builder, recipe, iFocusGroup, 11, 25, 84, 25);
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 47, 25)
        .addIngredients(Ingredient.of(RootsTags.Items.RUNIC_SHEARS));
  }

  @Override
  protected ItemStack getTool() {
    return new ItemStack(ModItems.RUNIC_SHEARS);
  }
}