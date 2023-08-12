package mysticmods.roots.api.recipe;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import mysticmods.roots.api.capability.Grant;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.crafting.IEntityCrafting;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.advancements.Advancement;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public abstract class EntityRecipe<W extends IEntityCrafting> extends RootsRecipeBase implements IEntityRecipe<W> {
  protected EntityTest test;

  public EntityRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }

  @Override
  public NonNullList<Ingredient> getBaseIngredients() {
    return NonNullList.create();
  }

  @Override
  public void setEntityTest(EntityTest test) {
    this.test = test;
  }

  @Override
  public EntityTest getEntityTest() {
    return test;
  }

  // TODO: Ensure that not copying this item won't cause problems
  @Override
  public ItemStack getResultItem() {
    if (result == null) {
      return ItemStack.EMPTY;
    }
    return result;
  }

  @Override
  public boolean matches(W pContainer, Level pLevel) {
    return test.test(pContainer.getEntity());
  }


  @Override
  public ResourceLocation getId() {
    return recipeId;
  }

  public void modifyEntity (W pContainer) {
  }

  @Override
  public ItemStack assemble(W pInv) {
    Level level = pInv.getLevel();
    if (level == null) {
      throw new IllegalStateException("Cannot assemble recipe `" + getId() + "` without a world!");
    }
    if (!level.isClientSide()) {
      modifyEntity(pInv);
      Player player = pInv.getPlayer();
      if (player != null) {
        for (Grant grant : getGrants()) {
          grant.grant((ServerPlayer) player);
        }
      }
    }

    return getResultItem().copy();
  }

  public abstract static class Serializer<W extends IEntityCrafting, R extends EntityRecipe<W>> extends RootsSerializerBase implements RecipeSerializer<R> {

    private final EntityRecipeBuilder<R> builder;

    public Serializer(EntityRecipeBuilder<R> builder) {
      this.builder = builder;
    }

    protected void fromJsonAdditional(R recipe, ResourceLocation pRecipeId, JsonObject pJson) {
    }

    @Override
    public R fromJson(ResourceLocation pRecipeId, JsonObject pJson) {
      R recipe = builder.create(pRecipeId);
      baseFromJson(recipe, pRecipeId, pJson);
      recipe.test = EntityTest.CODEC.get().parse(JsonOps.INSTANCE, pJson.get("test")).result().orElseThrow(() -> new IllegalStateException("Could not parse entity test for recipe " + pRecipeId));
      fromJsonAdditional(recipe, pRecipeId, pJson);
      return recipe;
    }

    protected void fromNetworkAdditional(R recipe, ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
    }

    @Nullable
    @Override
    public R fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
      R recipe = builder.create(pRecipeId);
      baseFromNetwork(recipe, pRecipeId, pBuffer);
      recipe.test = pBuffer.readWithCodec(EntityTest.CODEC.get());
      fromNetworkAdditional(recipe, pRecipeId, pBuffer);
      return recipe;
    }

    protected void toNetworkAdditional(R recipe, FriendlyByteBuf pBuffer) {
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, R recipe) {
      baseToNetwork(pBuffer, recipe);
      pBuffer.writeWithCodec(EntityTest.CODEC.get(), recipe.test);
      // TODO: etc
      toNetworkAdditional(recipe, pBuffer);
    }
  }

  public abstract static class Builder extends RootsRecipeBuilderBase {
    protected EntityTest test;

    protected Builder() {
    }

    @Override
    protected boolean allowEmptyOutput() {
      return true;
    }

    protected Builder(ItemStack result) {
      super(result);
    }

    @Override
    public abstract RecipeSerializer<?> getSerializer();

    public Builder setTest(EntityTest test) {
      this.test = test;
      return this;
    }

    @Override
    public void doSave(Consumer<FinishedRecipe> consumer, ResourceLocation recipeName) {
      consumer.accept(new EntityRecipe.Builder.Result(recipeName, result, test, chanceOutputs, grants, levelConditions, playerConditions, getSerializer(), advancement, getAdvancementId(recipeName)));
    }

    public static class Result extends RootsResultBase {
      private final EntityTest test;

      public Result(ResourceLocation id, ItemStack result, EntityTest test, List<ChanceOutput> chanceOutputs, List<Grant> grants, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, RecipeSerializer<?> serializer, Advancement.Builder advancementBuilder, ResourceLocation advancementId) {
        super(id, result, Collections.emptyList(), chanceOutputs, grants, levelConditions, playerConditions, serializer, advancementBuilder, advancementId);
        this.test = test;
      }

      @Override
      public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        json.add("test", EntityTest.CODEC.get().encodeStart(JsonOps.INSTANCE, test).getOrThrow(false, s -> {
          throw new IllegalStateException(s);
        }));
      }
    }
  }

  @FunctionalInterface
  public interface EntityRecipeBuilder<R extends EntityRecipe<?>> {
    R create(ResourceLocation recipeId);
  }
}
