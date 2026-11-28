package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import mysticmods.roots.util.Pos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;

public class RunicEntityCategory extends RootsDefaultCategory<RunicEntityRecipe> {
  public RunicEntityCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.RUNIC_ENTITY_RECIPE_TYPE, helper, () -> new ItemStack(ModItems.RUNIC_SHEARS.get()), Component.translatable("roots.jei.runic_entity"));
  }

  @Override
  protected void addInputs(IRecipeLayoutBuilder builder, RunicEntityRecipe recipe, IFocusGroup iFocusGroup, Pos pos) {
    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

    for (EntityType<?> entityType : recipe.getEntityTest().getEntityTypes()) {
      SpawnEggItem inputItem = DeferredSpawnEggItem.byId(entityType);
      collector.addIngredients(Ingredient.of(inputItem));
    }

    List<RootsEntityType> types = recipe.getEntityTest().getEntityTypes().stream().map(RootsEntityType::new).toList();

    builder.addSlot(RecipeIngredientRole.INPUT, 12, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.MAIN_ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    super.addInputs(builder, recipe, iFocusGroup, pos);
  }

  @Override
  protected void addDevice(IRecipeLayoutBuilder builder) {
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 80, 26)
        .addIngredients(Ingredient.of(RootsTags.Items.RUNIC_SHEARS));
  }

  @Override
  protected ItemStack getTool() {
    return new ItemStack(ModItems.RUNIC_SHEARS);
  }
}
