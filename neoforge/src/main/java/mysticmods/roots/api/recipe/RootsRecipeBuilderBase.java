package mysticmods.roots.api.recipe;

import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeBuilder.ROOT_RECIPE_ADVANCEMENT;

// TODO: Check if the ItemStack means that NBT is supported
public abstract class RootsRecipeBuilderBase {
  protected final Advancement.Builder advancement = Advancement.Builder.advancement();
  protected ItemStack result;
  protected final List<Ingredient> ingredients = new ArrayList<>();
  protected final List<ChanceOutput> chanceOutputs = new ArrayList<>();
  protected final List<Grant> grants = new ArrayList<>();
  protected final List<LevelCondition> levelConditions = new ArrayList<>();
  protected final List<PlayerCondition> playerConditions = new ArrayList<>();

  protected RootsRecipeBuilderBase() {
  }

  protected abstract boolean allowEmptyOutput();

  protected abstract boolean requireIngredients();

  protected RootsRecipeBuilderBase(ItemStack result) {
    this.result = result;
  }

  public abstract RecipeSerializer<?> getSerializer();

  public RootsRecipeBuilderBase setOutput(ItemStack output) {
    this.result = output;
    return this;
  }

  public RootsRecipeBuilderBase addChanceOutput(ChanceOutput output) {
    this.chanceOutputs.add(output);
    return this;
  }

  public RootsRecipeBuilderBase addChanceOutputs(Collection<ChanceOutput> output) {
    this.chanceOutputs.addAll(output);
    return this;
  }

  public RootsRecipeBuilderBase addChanceOutput(ItemStack output, float chance) {
    return addChanceOutput(new ChanceOutput(output, chance));
  }

  public RootsRecipeBuilderBase addIngredient(TagKey<Item> ingredient) {
    addIngredient(Ingredient.of(ingredient));
    return this;
  }

  public RootsRecipeBuilderBase addIngredient(ItemLike item) {
    addIngredient(Ingredient.of(item));
    return this;
  }

  public RootsRecipeBuilderBase addIngredient(Ingredient ingredient) {
    this.ingredients.add(ingredient);
    return this;
  }

  public RootsRecipeBuilderBase addGrant(Grant grant) {
    this.grants.add(grant);
    return this;
  }

  public RootsRecipeBuilderBase addLevelCondition(LevelCondition condition) {
    this.levelConditions.add(condition);
    return this;
  }

  public RootsRecipeBuilderBase addPlayerCondition(PlayerCondition condition) {
    this.playerConditions.add(condition);
    return this;
  }

  public RootsRecipeBuilderBase unlockedBy(String criterionName, CriterionTriggerInstance pCriterionTrigger) {
    this.advancement.addCriterion(criterionName, pCriterionTrigger);
    return this;
  }

  protected boolean hasOutput() {
    return (result != null && !result.isEmpty()) || !chanceOutputs.isEmpty();
  }

  protected void validate(ResourceLocation recipeName) {
    if (!hasOutput() && !allowEmptyOutput() && grants.isEmpty()) {
      throw new IllegalStateException("No output, chance output or grants defined for recipe " + recipeName);
    }
    if (ingredients.isEmpty() && requireIngredients()) {
      throw new IllegalStateException("No ingredients defined for recipe " + recipeName);
    }
    if (advancement.getCriteria().isEmpty()) {
      throw new IllegalStateException("No way of obtaining recipe " + recipeName);
    }
  }

  protected String getFolderName(ResourceLocation recipeName) {
    if (result != null) {
      return result.getItem().getItemCategory().getRecipeFolderName();
    } else if (!chanceOutputs.isEmpty()) {
      return chanceOutputs.get(0).getOutput().getItem().getItemCategory().getRecipeFolderName();
    } else if (!grants.isEmpty()) {
      return grants.get(0).getId().getNamespace();
    } else {
      return recipeName.getNamespace();
    }
  }

  protected ResourceLocation getAdvancementId(ResourceLocation recipeName) {
    return new ResourceLocation(recipeName.getNamespace(), "recipes/" + getFolderName(recipeName) + "/" + recipeName.getPath());
  }

  public void save(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
    validate(recipeName);
    advancement.parent(ROOT_RECIPE_ADVANCEMENT).addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeName)).rewards(AdvancementRewards.Builder.recipe(recipeName)).requirements(RequirementsStrategy.OR);
    doSave(consumer, recipeName);
  }

  public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
    consumer.accept(new RootsResultBase(recipeName, result, ingredients, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName)));
  }
}
