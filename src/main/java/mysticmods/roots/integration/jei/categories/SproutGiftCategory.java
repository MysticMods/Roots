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
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.integration.jei.fake.SproutGiftRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;

public class SproutGiftCategory extends RootsRecipeBaseCategory<SproutGiftRecipe> {
  public SproutGiftCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.SPROUT_GIFTS_RECIPE_TYPE, helper, 128, 163, RootsAPI.rl("textures/gui/jei/loot.png"), () -> new ItemStack(ModItems.AUBERGINE.get()), Component.translatable("roots.jei.sprout_gifts"));
  }

  @Override
  public List<ChanceOutput> getChanceOutputs(SproutGiftRecipe recipe) {
    return recipe.outputs();
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, SproutGiftRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

    SpawnEggItem inputItem = DeferredSpawnEggItem.byId(recipe.sprout());
    collector.addIngredients(Ingredient.of(inputItem));
    List<RootsEntityType> types = List.of(new RootsEntityType(recipe.sprout()));
    builder.addSlot(RecipeIngredientRole.INPUT, 47, 12)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.MAIN_ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    int row = 0;
    int column = 0;

    List<ChanceOutput> outputs = recipe.outputs();

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 7 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 2 + column * 18, 72 + row * 18)
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, SproutGiftRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);
  }
}
