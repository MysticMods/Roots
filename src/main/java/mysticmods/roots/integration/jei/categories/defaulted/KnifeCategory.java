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
import mysticmods.roots.recipe.knife.KnifeOffHandRecipe;
import mysticmods.roots.recipe.knife.KnifeRecipe;
import mysticmods.roots.util.Pos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

// TODO: Durability cost
public class KnifeCategory extends RootsDefaultCategory<KnifeRecipe> {
  private static ItemStack knife;

  public KnifeCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.KNIFE_RECIPE_TYPE, helper, () -> new ItemStack(ModItems.IRON_KNIFE.get()), Component.translatable("roots.jei.knife_crafting"), RootsAPI.rl("textures/gui/jei/default_block.png"));
  }

  /*public static void generateDynamicIngredients() {
    if (knife == null) {
      knife = new ItemStack(ModItems.NETHERITE_KNIFE);
    }
  }*/

  @Override
  protected void addInputs(IRecipeLayoutBuilder builder, KnifeRecipe recipe, IFocusGroup iFocusGroup, Pos pos) {
    super.addInputs(builder, recipe, iFocusGroup, pos);

    // TODO: Input slot is the wrong place
    if (recipe instanceof KnifeOffHandRecipe offHandRecipe) {
      builder.addSlot(RecipeIngredientRole.INPUT, 39, 11)
          .addIngredients(Ingredient.of(offHandRecipe.getOffHandTag()));
    }

    WorldRecipeUtil.setWorldRecipe(builder, recipe, iFocusGroup, 7, 34, 73, 34);
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 47, 25)
        .addIngredients(Ingredient.of(RootsTags.Items.KNIVES));
  }
}
