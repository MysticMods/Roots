package mysticmods.roots.integration.jei.categories.defaulted;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.ConditionIngredient;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.grove.power.consumer.PowerRequest;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.recipe.output.Cooldown;
import mysticmods.roots.integration.jei.RootsJEIPlugin;
import mysticmods.roots.integration.jei.categories.RootsRecipeBaseCategory;
import mysticmods.roots.integration.jei.ingredient.CustomIngredient;
import mysticmods.roots.integration.jei.ingredient.grove.GroveGeneratedPower;
import mysticmods.roots.util.Pos;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class RootsDefaultCategory<T extends RootsRecipe<?, ?>> extends RootsRecipeBaseCategory<T> implements IPositioned {
  public RootsDefaultCategory(RecipeType<T> recipeType, IGuiHelper helper, Supplier<ItemStack> icon, Component title, ResourceLocation background) {
    this(recipeType, helper, 0, 0, background, icon, title);
  }

  public RootsDefaultCategory(RecipeType<T> recipeType, IGuiHelper helper, Supplier<ItemStack> icon, Component title) {
    this(recipeType, helper, 0, 0, null, icon, title);
  }

  public RootsDefaultCategory(RecipeType<T> recipeType, IGuiHelper helper, int width, int height, @Nullable ResourceLocation background, Supplier<ItemStack> icon, Component title) {
    super(recipeType, helper, width == 0 ? 178 : width, height == 0 ? 68 : height, background == null ? RootsAPI.rl("textures/gui/jei/default.png") : background, icon, title);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup) {
    addInputs(builder, recipe, iFocusGroup, new Pos());
    addOutputs(builder, recipe, iFocusGroup, new Pos());
    addDevice(builder);
  }

  protected void addInputs(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup, Pos pos) {
    List<CustomIngredient<?>> ingredients = gatherInputs(recipe);

    for (int i = 0; i < ingredients.size(); i++) {
      if (i % getIngredientColumnCount() == 0 && i != 0) {
        pos.row++;
        pos.column = 0;
      }
      CustomIngredient<?> ingredient = ingredients.get(i);
      ingredient.fill(pos, builder, this);
      pos.column++;
    }
  }

  protected void addOutputs(IRecipeLayoutBuilder builder, T recipe, IFocusGroup iFocusGroup, Pos pos) {
    List<CustomIngredient<?>> outputs = gatherOutputs(recipe);

    for (int i = 0; i < outputs.size(); i++) {
      if (i % getOutputColumnCount() == 0 && i != 0) {
        pos.row++;
        pos.column = 0;
      }
      CustomIngredient<?> ingredient = outputs.get(i);
      ingredient.fill(pos, builder, this);
      pos.column++;
    }
  }

  @Override
  public int getIngredientWidth() {
    return 16;
  }

  @Override
  public int getIngredientXPadding() {
    return 2;
  }

  @Override
  public int getIngredientYPadding() {
    return 2;
  }

  @Override
  public int getIngredientHeight() {
    return 16;
  }

  @Override
  public int getIngredientColumnCount(){
    return 4;
  }

  @Override
  public int getOutputWidth() {
    return 16;
  }

  @Override
  public int getOutputHeight() {
    return 16;
  }

  @Override
  public int getOutputYPadding() {
    return 2;
  }

  @Override
  public int getOutputXPadding() {
    return 112;
  }

  @Override
  public int getOutputColumnCount() {
    return 4;
  }

  protected abstract void addDevice(IRecipeLayoutBuilder builder);

  protected ItemStack getTool () {
    return ItemStack.EMPTY;
  }

  public List<CustomIngredient<?>> gatherInputs(T recipe) {
    List<CustomIngredient<?>> ingredients = new ArrayList<>();

    // Actual ingredients
    for (Ingredient ingredient : recipe.getIngredients()) {
      ingredients.add(new CustomIngredient<>(RecipeIngredientRole.INPUT, VanillaTypes.ITEM_STACK, List.of(ingredient.getItems())));
    }

    // Power requirements
    for (PowerRequest power : recipe.getPowerRequirements()) {
      ingredients.add(new CustomIngredient<>(RecipeIngredientRole.INPUT, RootsJEIPlugin.GROVE_POWER_TYPE, GroveGeneratedPower.fromRequest(power), RootsJEIPlugin.GROVE_POWER_RENDERER));
    }

    // Conditions
    for (Holder<ICondition> condHolder : recipe.getConditions()) {
      List<ConditionIngredient> values = condHolder.value().getIngredient(condHolder);
      if (values != null && !values.isEmpty()) {
        ingredients.add(new CustomIngredient<>(RecipeIngredientRole.INPUT, RootsJEIPlugin.CONDITION_INGREDIENT, values, RootsJEIPlugin.CONDITION_RENDERER, CustomIngredient.CONDITION_ITEM_STACK_BUILDER));
      }
    }

    if (recipe.hasDurabilityCost()) {
      ItemStack tool = getTool();
      if (tool.isEmpty()) {
        throw new IllegalStateException("Recipe has a durability cost but no tool: " + recipe);
      }

      // TODO: Custom renderer
      tool.setDamageValue(tool.getMaxDamage() - recipe.getDurabilityCost());
      ingredients.add(new CustomIngredient<>(RecipeIngredientRole.INPUT, VanillaTypes.ITEM_STACK, List.of(tool)));
    }

    return ingredients;
  }

  public List<CustomIngredient<?>> gatherOutputs (T recipe) {
    Minecraft minecraft = Minecraft.getInstance();
    if (minecraft.level == null) {
      throw new IllegalStateException("No level in Minecraft");
    }
    HolderLookup.Provider provider = minecraft.level.registryAccess();

    List<CustomIngredient<?>> outputs = new ArrayList<>();

    if (recipe.hasOutput(provider)) {
      outputs.add(new CustomIngredient<>(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, List.of(recipe.getResultItem(provider))));
    }

    if (recipe.hasChanceOutputs(provider)) {
      for (ChanceOutput chanceOutput : recipe.getChanceOutputs()) {
        outputs.add(new CustomIngredient<>(RecipeIngredientRole.OUTPUT, VanillaTypes.ITEM_STACK, List.of(chanceOutput.output()), null, CustomIngredient.chance(chanceOutput)));
      }
    }

    if (recipe.hasCooldown()) {
      outputs.add(new CustomIngredient<>(RecipeIngredientRole.OUTPUT, RootsJEIPlugin.COOLDOWN_INGREDIENT, List.of(new Cooldown(recipe.getCooldown())), RootsJEIPlugin.COOLDOWN_RENDERER));
    }

    return outputs;
  }
}
