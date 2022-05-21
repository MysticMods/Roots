package mysticmods.roots.recipe.mortar;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mysticmods.roots.api.recipe.ConditionalOutput;
import mysticmods.roots.api.recipe.Grant;
import mysticmods.roots.api.recipe.RootsRecipe;
import mysticmods.roots.api.recipe.RootsTileRecipe;
import mysticmods.roots.block.entity.MortarBlockEntity;
import mysticmods.roots.init.ModRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.VineBlock;
import net.minecraftforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

// TODO: Conditional outputs
public class MortarRecipe extends RootsTileRecipe<MortarInventory, MortarBlockEntity, MortarCrafting> {
  private final List<ConditionalOutput> conditionalOutputs = new ArrayList<>();
  private int times;
  private final List<Grant> grants = new ArrayList<>();

  public MortarRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }

  public int getTimes() {
    return times;
  }

  public void setTimes(int times) {
    this.times = times;
  }

  public void addConditionalOutput(ConditionalOutput output) {
    conditionalOutputs.add(output);
  }

  public void addConditionalOutputs(Collection<ConditionalOutput> outputs) {
    conditionalOutputs.addAll(outputs);
  }

  public void addGrant (Grant grant) {
    grants.add(grant);
  }

  public void addGrants (Collection<Grant> grants) {
    this.grants.addAll(grants);
  }

  public List<ConditionalOutput> getConditionalOutputs() {
    return conditionalOutputs;
  }

  public List<Grant> getGrants() {
    return grants;
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
  public ItemStack assemble(MortarCrafting pInv) {
    Player player = pInv.getPlayer();
    if (player != null && player.level.isClientSide()) {
      for (Grant grant : getGrants()) {
        grant.accept((ServerPlayer) player);
      }
    }

    return result.copy();
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
      if (GsonHelper.isArrayNode(pJson, "conditional_outputs")) {
        List<ConditionalOutput> outputs = new ArrayList<>();
        JsonArray conditionalOutputs = GsonHelper.getAsJsonArray(pJson, "conditional_outputs");
        for (int i = 0; i < conditionalOutputs.size(); i++) {
          if (conditionalOutputs.get(i).isJsonObject()) {
            outputs.add(ConditionalOutput.fromJson(conditionalOutputs.get(i)));
          } else {
            throw new JsonSyntaxException("Invalid conditional output: " + conditionalOutputs.get(i));
          }
        }
        recipe.addConditionalOutputs(outputs);
      }
      if (GsonHelper.isArrayNode(pJson, "grants")) {
        List<Grant> grants = new ArrayList<>();
        JsonArray thisGrants = GsonHelper.getAsJsonArray(pJson, "grants");
        for (int i = 0; i < thisGrants.size(); i++) {
          if (thisGrants.get(i).isJsonObject()) {
            grants.add(Grant.fromJson(thisGrants.get(i)));
          } else {
            throw new JsonSyntaxException("Invalid grant: " + thisGrants.get(i));
          }
        }
        recipe.addGrants(grants);
      }
    }

    @Override
    protected void fromNetworkAdditional(MortarRecipe recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      super.fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      recipe.setTimes(pBuffer.readVarInt());
      int count = pBuffer.readVarInt();
      for (int i = 0; i < count; i++) {
        recipe.addConditionalOutput(ConditionalOutput.fromNetwork(pBuffer));
      }
      count = pBuffer.readVarInt();
      for (int i = 0; i < count; i++) {
        recipe.addGrant(Grant.fromNetwork(pBuffer));
      }
    }

    @Override
    protected void toNetworkAdditional(MortarRecipe recipe, FriendlyByteBuf pBuffer) {
      super.toNetworkAdditional(recipe, pBuffer);
      pBuffer.writeVarInt(recipe.getTimes());
      List<ConditionalOutput> conditionalOutputs = recipe.getConditionalOutputs();
      pBuffer.writeVarInt(conditionalOutputs.size());
      for (ConditionalOutput output : conditionalOutputs) {
        output.toNetwork(pBuffer);
      }
      List<Grant> grants = recipe.getGrants();
      pBuffer.writeVarInt(grants.size());
      for (Grant grant : grants) {
        grant.toNetwork(pBuffer);
      }
    }
  }

  public static class Builder extends RootsRecipe.Builder {
    private final List<ConditionalOutput> conditionalOutputs = new ArrayList<>();
    private final List<Grant> grants = new ArrayList<>();
    private final int times;

    protected Builder(ItemLike item, int count, int times) {
      super(item, count);
      this.times = times;
    }

    public void addConditionalOutput(ItemStack output, float chance) {
      addConditionalOutput(new ConditionalOutput(output, chance));
    }

    public void addConditionalOutput(ConditionalOutput output) {
      conditionalOutputs.add(output);
    }

    public void grants (Grant grant) {
      grants.add(grant);
    }

    @Override
    public void build(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new Result(recipeName, result, count, ingredients, getSerializer(), times, conditionalOutputs, grants));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
      return ModRecipes.Serializers.MORTAR.get();
    }

    public static class Result extends RootsRecipe.Builder.Result {
      private final List<ConditionalOutput> conditionalOutputs;
      private final List<Grant> grants;
      private final int times;

      public Result(ResourceLocation id, Item result, int count, List<Ingredient> ingredients, RecipeSerializer<?> serializer, int times, List<ConditionalOutput> conditionalOutputs, List<Grant> grants) {
        super(id, result, count, ingredients, serializer);
        this.times = times;
        this.conditionalOutputs = conditionalOutputs;
        this.grants = grants;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);
        json.addProperty("times", times);
        if (!conditionalOutputs.isEmpty()) {
          JsonArray outputs = new JsonArray();
          for (ConditionalOutput output : conditionalOutputs) {
            outputs.add(output.toJson());
          }
          json.add("conditional_outputs", outputs);
        }
        if (!grants.isEmpty()) {
          JsonArray grants = new JsonArray();
          for (Grant grant : this.grants) {
            grants.add(grant.toJson());
          }
          json.add("grants", grants);
        }
      }
    }
  }

  public static Builder builder(ItemLike item, int count, int times) {
    return new Builder(item, count, times);
  }
}
