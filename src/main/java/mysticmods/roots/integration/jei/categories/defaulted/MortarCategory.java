package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.recipe.mortar.MortarRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public abstract class MortarCategory extends RootsDefaultCategory<MortarRecipe> {
  public MortarCategory(RecipeType<MortarRecipe> type, Component title, IGuiHelper helper) {
    super(type, helper, () -> new ItemStack(ModBlocks.MORTAR.get()), title);
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(ModBlocks.MORTAR.get()));
  }

  public static class Regular extends MortarCategory {
    public Regular(IGuiHelper helper) {
      super(RootsJEIPlugin.MORTAR_RECIPE_TYPE, Component.translatable("roots.jei.mortar_crafting"), helper);
    }
  }

  public static class Spell extends MortarCategory {
    public Spell(IGuiHelper helper) {
      super(RootsJEIPlugin.MORTAR_SPELL_RECIPE_TYPE, Component.translatable("roots.jei.mortar_spell_crafting"), helper);
    }
  }
}