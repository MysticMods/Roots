package mysticmods.roots.recipe.runic;

import com.google.gson.JsonObject;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.EntityRecipe;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.init.ModRecipes;
import mysticmods.roots.init.ModSerializers;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

import java.util.List;
import java.util.function.Consumer;

public class RunicEntityRecipe extends EntityRecipe<RunicEntityCrafting> {
  private int cooldown;
  private int durabilityCost = 1;

  public RunicEntityRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  public int getCooldown() {
    return cooldown;
  }

  public int getDurabilityCost() {
    return durabilityCost;
  }

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }

  public void setDurabilityCost(int durabilityCost) {
    this.durabilityCost = durabilityCost;
  }

  @Override
  public ItemStack getBaseResultItem() {
    return getResultItem();
  }

  @Override
  public void setIngredients(NonNullList<Ingredient> ingredients) {
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModSerializers.RUNIC_ENTITY.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.RUNIC_ENTITY.get();
  }

  public static class Serializer extends EntityRecipe.Serializer<RunicEntityCrafting, RunicEntityRecipe> {
    public Serializer() {
      super(RunicEntityRecipe::new);
    }

    @Override
    protected void fromJsonAdditional(RunicEntityRecipe recipe, ResourceLocation pRecipeId, JsonObject pJson) {
      super.fromJsonAdditional(recipe, pRecipeId, pJson);
      if (pJson.has("durability_cost")) {
        recipe.setDurabilityCost(GsonHelper.getAsInt(pJson, "durability_cost"));
      }
      if (pJson.has("cooldown")) {
        recipe.setCooldown(GsonHelper.getAsInt(pJson, "cooldown"));
      }
    }

    @Override
    protected void fromNetworkAdditional(RunicEntityRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      recipe.setCooldown(pBuffer.readVarInt());
      recipe.setDurabilityCost(pBuffer.readVarInt());
    }

    @Override
    protected void toNetworkAdditional(RunicEntityRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeVarInt(recipe.cooldown);
      pBuffer.writeVarInt(recipe.durabilityCost);
    }
  }

  public static class Builder extends EntityRecipe.Builder {
    protected int cooldown;
    protected int durabilityCost;

    public Builder() {
    }

    public Builder(ItemStack result) {
      super(result);
    }

    @Override
    protected boolean requireIngredients() {
      return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModSerializers.RUNIC_ENTITY.get();
    }

    public Builder setCooldown(int cooldown) {
      this.cooldown = cooldown;
      return this;
    }

    public Builder setDurabilityCost(int durabilityCost) {
      this.durabilityCost = durabilityCost;
      return this;
    }

    @Override
    public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, test, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName), cooldown, durabilityCost));
    }

    public static final class Result extends EntityRecipe.Builder.Result {
      private final int cooldown;
      private final int durabilityCost;

      public Result(ResourceLocation id, ItemStack result, EntityTest test, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId, int cooldown, int durabilityCost) {
        super(id, result, test, chanceOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
        this.cooldown = cooldown;
        this.durabilityCost = durabilityCost;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.addProperty("cooldown", cooldown);
        json.addProperty("durability_cost", durabilityCost);
      }
    }
  }


  public static Builder builder(ItemStack stack) {
    return new Builder(stack);
  }

  public static Builder builder(ItemLike item, int count) {
    return new Builder(new ItemStack(item, count));
  }

  public static Builder builder(ItemLike item) {
    return builder(item, 1);
  }
}
