package mysticmods.roots.recipe.grove;

import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsRecipeBuilderBase;
import mysticmods.roots.api.recipe.RootsResultBase;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.api.reference.Identifiers;
import mysticmods.roots.blockentity.GroveCrafterBlockEntity;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

public class GroveRecipe extends RootsTileRecipe<GroveInventoryWrapper, GroveCrafterBlockEntity, GroveCrafting> {
  public GroveRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public ItemStack assemble(GroveCrafting pContainer) {
    return super.assemble(pContainer);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.GROVE_CRAFTING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.GROVE.get();
  }

  @Override
  public String getGroup() {
    return Identifiers.GROVE_RECIPE_GROUP;
  }

  public static class Serializer extends RootsRecipe.Serializer<GroveInventoryWrapper, GroveCrafting, GroveRecipe> {
    public Serializer() {
      super(GroveRecipe::new);
    }
  }

  public static class Builder extends RootsRecipeBuilderBase {

    @Override
    protected boolean allowEmptyOutput() {
      return false;
    }

    @Override
    protected boolean requireIngredients() {
      return true;
    }

    protected Builder(ItemStack result) {
      super(result);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.GROVE_CRAFTING.get();
    }
  }

  public static class MultiBuilder extends Builder {
    protected final int count;

    protected MultiBuilder(ItemStack result, int count) {
      super(result);
      if (count <= 1) {
        throw new IllegalArgumentException("Multi-recipe builder count must be greater than 1");
      }
      this.count = count;
    }

    @Override
    protected void validate(ResourceLocation recipeName) {
      if (ingredients.size() != 1) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' must have exactly one ingredient");
      }
      if (!grants.isEmpty()) {
        throw new IllegalStateException("Multi-recipe '" + recipeName + "' can't have grants");
      }
      super.validate(recipeName);
    }

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      validate(recipeName);
      Ingredient ingredient = ingredients.get(0);
      for (int i = 1; i < count + 1; i++) {
        ResourceLocation thisRecipeName = new ResourceLocation(recipeName.getNamespace(), recipeName.getPath() + "_" + i);
        ItemStack thisResult = result.copy();
        thisResult.setCount(i);
        List<Ingredient> thisIngredients = new ArrayList<>();
        for (int j = 0; j < i; j++) {
          thisIngredients.add(ingredient);
        }
        Advancement.Builder thisAdvancement = Advancement.Builder.advancement();
        for (Map.Entry<String, Criterion> entry : advancement.getCriteria().entrySet()) {
          thisAdvancement.addCriterion(entry.getKey(), entry.getValue());
        }
        thisAdvancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(thisRecipeName)).rewards(AdvancementRewards.Builder.recipe(thisRecipeName)).requirements(RequirementsStrategy.OR);
        consumer.accept(new RootsResultBase(thisRecipeName, thisResult, thisIngredients, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), thisAdvancement, getAdvancementId(thisRecipeName)));
      }
    }
  }

  public static Builder builder(ItemStack stack) {
    return new Builder(stack);
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder (ItemLike item) {
    return builder(item, 1);
  }

  public static MultiBuilder multiBuilder (ItemLike item, int count) {
    return new MultiBuilder(new ItemStack(item), count);
  }
}
