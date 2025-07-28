package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityType;
import mysticmods.roots.integration.jei.categories.widget.InfoWidget;
import mysticmods.roots.recipe.AnimalHarvestRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.ArrayList;
import java.util.List;

public class AnimalHarvestCategory extends RootsRecipeBaseCategory<AnimalHarvestRecipe> {
  public AnimalHarvestCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.ANIMAL_HARVEST_RECIPE_TYPE, helper, 165, 83, RootsAPI.rl("textures/gui/jei/animal_harvest.png"), () -> new ItemStack(ModItems.RITUAL_ANIMAL_HARVEST.get()), Component.translatable("roots.jei.animal_harvest"));
  }

  @Override
  public List<ChanceOutput> getChanceOutputs(AnimalHarvestRecipe recipe) {
    return recipe.loot();
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, AnimalHarvestRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

      SpawnEggItem inputItem = DeferredSpawnEggItem.byId(recipe.entity());
      collector.addIngredients(Ingredient.of(inputItem));

    List<RootsEntityType> types = List.of(new RootsEntityType(recipe.entity()));

    builder.addSlot(RecipeIngredientRole.INPUT, 12, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    int row = 0;
    int column = 0;

    for (int i = 0; i < recipe.loot().size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 105 + column * 17, 2 + row * 17)
          .addItemStack(recipe.loot().get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, AnimalHarvestRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    builder.addWidget(new InfoWidget(104-11, 2, Component.translatable("roots.jei.animal_harvest.info")));
  }
}
