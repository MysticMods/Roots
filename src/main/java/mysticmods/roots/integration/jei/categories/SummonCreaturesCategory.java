package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.IPlayerCondition;
import mysticmods.roots.api.recipe.ComplexEntityType;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.integration.jei.widget.LevelConditionWidget;
import mysticmods.roots.integration.jei.widget.PlayerConditionWidget;
import mysticmods.roots.recipe.pyre.SummonCreaturesRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;

public class SummonCreaturesCategory extends RootsRecipeBaseCategory<SummonCreaturesRecipe> {
  public SummonCreaturesCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.SUMMON_CREATURES_RECIPE_TYPE, helper, 166, 128, RootsAPI.rl("textures/gui/jei/summon_creatures.png"), () -> new ItemStack(ModItems.RITUAL_SUMMON_CREATURES.get()), Component.translatable("roots.jei.summon_creatures"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, SummonCreaturesRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    ComplexEntityType entityType = recipe.getEntity();

    // TODO: ???
    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT);
    collector.addIngredients(Ingredient.of(DeferredSpawnEggItem.byId(entityType.type())));

    List<RootsEntityType> types = List.of(new RootsEntityType(entityType.type()));

    builder.addSlot(RecipeIngredientRole.OUTPUT, 117, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.MAIN_ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    List<ChanceOutput> outputs = recipe.getCachedOutputs();
    if (!outputs.isEmpty()) {
      RootsAPI.LOG.error("Summon Creatures recipe {} has chance outputs, this is not expected!");
    }

    builder.addSlot(RecipeIngredientRole.INPUT, 74, 52)
        .addIngredients(Ingredient.of(RootsTags.Items.RITUAL_PEDESTALS));

    List<Ingredient> inputs = recipe.getIngredients();

    int row = 0;
    int column = 0;

    for (int i = 0; i < inputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.INPUT, 2 + column * 17, 2 + row * 17)
          .addIngredients(inputs.get(i)).setSlotName(String.valueOf(i));
      /*          .addRichTooltipCallback(this.richestTooltip(recipe));*/
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, SummonCreaturesRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

    int column = 0;
    for (ILevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 71;
      if (count == 4) {
        offset = 76;
      }
      if (count == 3) {
        offset = 79;
      }
      builder.addWidget(new LevelConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getNameComponent(), condition.getDescriptionComponent()));
      column++;
    }
    int row = 0;
    for (IPlayerCondition condition : recipe.getPlayerConditions()) {
      builder.addWidget(PlayerConditionWidget.create(getWidth(), 78 + row * 18, condition));
      row++;
    }
  }
}
