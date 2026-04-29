package mysticmods.roots.integration.jei.categories;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.ingredient.entity.RootsEntityType;
import mysticmods.roots.integration.jei.widget.CooldownWidget;
import mysticmods.roots.recipe.fake.EntityInteractionRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;

import java.util.List;

public class EntityInteractionCategory extends RootsRecipeBaseCategory<EntityInteractionRecipe> {
  public EntityInteractionCategory(IGuiHelper helper) {
    super(RootsJEIPlugin.ENTITY_INTERACTION_TYPE, helper, 166, 84, RootsAPI.rl("textures/gui/jei/entity_interaction.png"), () -> new ItemStack(Items.GLASS_BOTTLE), Component.translatable("roots.jei.entity_interaction"));
  }

  @Override
  public List<ChanceOutput> getChanceOutputs(EntityInteractionRecipe recipe) {
    return recipe.outputs();
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, EntityInteractionRecipe recipe, IFocusGroup iFocusGroup) {
    super.setRecipe(builder, recipe, iFocusGroup);

    var collector = builder.addInvisibleIngredients(RecipeIngredientRole.INPUT);

    for (EntityType<?> entityType : recipe.test().getEntityTypes()) {
      SpawnEggItem inputItem = DeferredSpawnEggItem.byId(entityType);
      collector.addIngredients(Ingredient.of(inputItem));
    }

    List<RootsEntityType> types = recipe.test().getEntityTypes().stream().map(RootsEntityType::new).toList();

    builder.addSlot(RecipeIngredientRole.INPUT, 12, 26)
        .setCustomRenderer(RootsJEIPlugin.ENTITY_TYPE, RootsJEIPlugin.MAIN_ENTITY_RENDERER)
        .addIngredients(RootsJEIPlugin.ENTITY_TYPE, types);

    List<ChanceOutput> outputs = recipe.outputs();

    int row = 0;
    int column = 0;

    builder.addSlot(RecipeIngredientRole.INPUT, 74, 34).addIngredients(recipe.input());

    for (int i = 0; i < outputs.size(); i++) {
      if (i % 4 == 0 && i != 0) {
        row++;
        column = 0;
      }
      builder.addSlot(RecipeIngredientRole.OUTPUT, 105 + column * 17, 2 + row * 17)
          .addItemStack(outputs.get(i).output()).setSlotName(String.valueOf(i))
          .addRichTooltipCallback(this.richestTooltip(recipe));
      column++;
    }
  }

  @Override
  public void createRecipeExtras(IRecipeExtrasBuilder builder, EntityInteractionRecipe recipe, IFocusGroup focuses) {
    super.createRecipeExtras(builder, recipe, focuses);

    if (recipe.cooldown() != -1) {
      Component cooldown = Component.translatable("roots.jei.text.cooldown", recipe.cooldown() / 20);
      builder.addWidget(new CooldownWidget(recipe.cooldown(), 70, 70, cooldown));
    }
  }
}
