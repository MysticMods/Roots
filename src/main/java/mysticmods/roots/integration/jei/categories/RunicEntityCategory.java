package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.drawable.DrawableComponent;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityRenderer;
import mysticmods.roots.integration.jei.categories.ingredient.RootsEntityType;
import mysticmods.roots.integration.jei.categories.widget.ConditionWidget;
import mysticmods.roots.integration.jei.categories.widget.DurabilityWidget;
import mysticmods.roots.recipe.runic.RunicEntityRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;
import java.util.stream.Collectors;

public class RunicEntityCategory extends RootsRecipeBaseCategory<RunicEntityRecipe> {
  public RunicEntityCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.RUNIC_ENTITY_RECIPE_TYPE, helper, 166, 124, RootsAPI.rl("textures/gui/jei/runic_entity.png"), () -> new ItemStack(ModItems.RUNIC_SHEARS.get()), Component.translatable("roots.jei.runic_entity"));
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, RunicEntityRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

    for (EntityType<?> entityType : recipe.getEntityTest().getEntityTypes()) {
      SpawnEggItem inputItem = DeferredSpawnEggItem.byId(entityType);
      collector.addIngredients(Ingredient.of(inputItem));
    }

    List<RootsEntityType> types = recipe.getEntityTest().getEntityTypes().stream().map(RootsEntityType::new).toList();

    builder.addSlot(RecipeIngredientRole.INPUT, 12, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    List<ChanceOutput> outputs = recipe.getCachedOutputs();

    int row = 0;
    int column = 0;

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 105 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).getOutput()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, RunicEntityRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    HolderLookup.Provider provider = Minecraft.getInstance().getConnection().registryAccess();

    if (recipe.getDurabilityCost() != -1) {
      Component durability = Component.translatable("roots.jei.text.durability", recipe.getDurabilityCost());
      builder.addWidget(new DurabilityWidget(recipe.getDurabilityCost(), 75, 50, durability));
    }

    int column = 0;
    for (LevelCondition condition : recipe.getLevelConditions()) {
      CanonicalRepresentation rep = condition.getRepresentation();
      int count = rep.getStates().size();
      int offset = 71;
      if (count == 4) {
        offset = 76;
      }
      if (count == 3) {
        offset = 79;
      }
      builder.addWidget(new ConditionWidget(column * 18, offset, 18, 40, rep.getStates(), condition.getName()));
      column++;
    }
  }
}
