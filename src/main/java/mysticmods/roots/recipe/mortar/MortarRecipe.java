package mysticmods.roots.recipe.mortar;

import com.google.gson.JsonObject;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.RecipeMatcher;
import noobanidus.libs.noobutil.ingredient.IngredientStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MortarRecipe extends RootsTileRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  private int times;

  public MortarRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  @Override
  public boolean matches(MortarCrafting pInv, Level pLevel) {
    List<ItemStack> inputs = new ArrayList<>();
    MortarInventory inv = pInv.getHandler();
    for (int i = 0; i < inv.getSlots(); i++) {
      ItemStack stack = inv.getStackInSlot(i);
      if (!stack.isEmpty()) {
        inputs.add(stack);
      }
    }

    return RecipeMatcher.findMatches(inputs, ingredients) != null;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return ModRecipes.Serializers.MORTAR.get();
  }

  @Override
  public RecipeType<?> getType() {
    return ModRecipes.Types.MORTAR;
  }

  public static class Serializer extends RootsRecipe.Serializer<MortarInventory, MortarCrafting, MortarRecipe> {
    public Serializer() {
      super(MortarRecipe::new);
    }

    @Override
    protected void fromJsonAdditional(MortarRecipe recipe, ResourceLocation pRecipeId, JsonObject pJson) {
      super.fromJsonAdditional(recipe, pRecipeId, pJson);
      recipe.setTimes(pJson.get("times").getAsInt());
    }

    @Override
    protected void fromNetworkAdditional(MortarRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      recipe.setTimes(pBuffer.readInt());
    }

    @Override
    protected void toNetworkAdditional(MortarRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeInt(recipe.getTimes());
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    private final int times;

    protected Builder(ItemLike item, int count, int times) {
      super(item, count);
      this.times = times;
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, count, ingredients, getSerializer(), times));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.MORTAR.get();
    }

    public static class Result extends RootsRecipe.Builder.Result {
      private final int times;
      public Result(ResourceLocation id, Item result, int count, List<IngredientStack> ingredients, RecipeSerializer<?> serializer, int times) {
        super(id, result, count, ingredients, serializer);
        this.times = times;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        json.addProperty("times", times);
      }
    }
  }

  public static Builder builder (ItemLike item, int count, int times) {
    return new Builder(item, count, times);
  }
}
