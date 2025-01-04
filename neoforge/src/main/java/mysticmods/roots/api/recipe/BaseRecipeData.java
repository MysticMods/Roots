package mysticmods.roots.api.recipe;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.capability.Unlock;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public class BaseRecipeData {
  public NonNullList<Ingredient> ingredients;
  public List<LevelCondition> levelConditions;
  public List<PlayerCondition> playerConditions;
  public ItemStack result;
  public List<ItemStack> results;
  public List<ChanceOutput> chanceOutputs;
  public List<Unlock<?>> unlocks;

  public BaseRecipeData() {
  }

  public BaseRecipeData(NonNullList<Ingredient> ingredients, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, ItemStack result, List<ItemStack> results, List<ChanceOutput> chanceOutputs, List<Unlock<?>> unlocks) {
    this.ingredients = ingredients;
    this.levelConditions = levelConditions;
    this.playerConditions = playerConditions;
    this.result = result;
    this.results = results;
    this.chanceOutputs = chanceOutputs;
    this.unlocks = unlocks;
  }

  public void updateFrom(BaseRecipeData data) {
    this.ingredients = data.ingredients;
    this.levelConditions = data.levelConditions;
    this.playerConditions = data.playerConditions;
    this.result = data.result;
    this.results = data.results;
    this.chanceOutputs = data.chanceOutputs;
    this.unlocks = data.unlocks;
  }

  public static final MapCodec<BaseRecipeData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC_NONEMPTY.listOf().fieldOf("ingredients").flatXmap(
          list -> {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(list.size(), Ingredient.EMPTY);
            for (int i = 0; i < list.size(); i++) {
              ingredients.set(i, list.get(i));
            }
            return DataResult.success(ingredients);
          },
          DataResult::success
      ).forGetter(o -> o.ingredients),
      LevelCondition.CODEC.listOf().fieldOf("levelConditions").forGetter(o -> o.levelConditions),
      PlayerCondition.CODEC.listOf().fieldOf("playerConditions").forGetter(o -> o.playerConditions),
      ItemStack.CODEC.fieldOf("result").forGetter(o -> o.result),
      ItemStack.CODEC.listOf().fieldOf("results").forGetter(o -> o.results),
      ChanceOutput.LIST_CODEC.fieldOf("chanceOutputs").forGetter(o -> o.chanceOutputs),
      Unlock.LIST_CODEC.fieldOf("grants").forGetter(o -> o.unlocks)
  ).apply(instance, BaseRecipeData::new));
  public static StreamCodec<RegistryFriendlyByteBuf, NonNullList<Ingredient>> INGREDIENT_LIST_STREAM = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));
  public static final StreamCodec<RegistryFriendlyByteBuf, BaseRecipeData> STREAM_CODEC = ExtraStreamCodecs.composite(
      // ingredients
      INGREDIENT_LIST_STREAM, o -> o.ingredients,
      LevelCondition.LIST_STREAM_CODEC, o -> o.levelConditions,
      PlayerCondition.LIST_STREAM_CODEC, o -> o.playerConditions,
      ItemStack.STREAM_CODEC, o -> o.result,
      ItemStack.LIST_STREAM_CODEC, o -> o.results,
      ChanceOutput.LIST_STREAM_CODEC, o -> o.chanceOutputs,
      Unlock.LIST_STREAM_CODEC, o -> o.unlocks,
      BaseRecipeData::new
  );

}
