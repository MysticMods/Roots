package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.recipe.ComplexEntityType;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;

public class SummonCreaturesCategory extends RootsDefaultCategory<SummonCreaturesRecipe> {
  public SummonCreaturesCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.SUMMON_CREATURES_RECIPE_TYPE, helper, () -> new ItemStack(ModItems.RITUAL_SUMMON_CREATURES.get()), Component.translatable("roots.jei.summon_creatures"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, SummonCreaturesRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    ComplexEntityType entityType = recipe.getEntity();

    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
    collector.addIngredients(Ingredient.of(DeferredSpawnEggItem.byId(entityType.type())));

    List<RootsEntityType> types = List.of(new RootsEntityType(entityType.type()));

    builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.MAIN_ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);
  }

  @Override
  protected void addOutputs(IRecipeLayoutBuilder builder, SummonCreaturesRecipe recipe, IFocusGroup iFocusGroup) {
    List<ChanceOutput> outputs = recipe.getCachedOutputs();
    if (!outputs.isEmpty()) {
      RootsAPI.LOG.error("Summon Creatures recipe {} has chance outputs, this is not expected!");
    }
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(RootsTags.Items.RITUAL_PEDESTALS));
  }
}
