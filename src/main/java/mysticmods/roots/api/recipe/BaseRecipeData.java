package mysticmods.roots.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.attachment.Unlock;
import mysticmods.roots.api.condition.LevelCondition;
import mysticmods.roots.api.condition.PlayerCondition;
import mysticmods.roots.api.recipe.output.ChanceOutput;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class BaseRecipeData {
  public static final MapCodec<BaseRecipeData> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Ingredient.CODEC_NONEMPTY.listOf().optionalFieldOf("ingredients", Collections.emptyList()).flatXmap(
          list -> {
            NonNullList<Ingredient> ingredients = NonNullList.withSize(list.size(), Ingredient.EMPTY);
            for (int i = 0; i < list.size(); i++) {
              ingredients.set(i, list.get(i));
            }
            return DataResult.success(ingredients);
          },
          DataResult::success
      ).forGetter(o -> o.ingredients),
      LevelCondition.CODEC.listOf().optionalFieldOf("levelConditions", Collections.emptyList())
          .forGetter(o -> o.levelConditions),
      PlayerCondition.CODEC.listOf().optionalFieldOf("playerConditions", Collections.emptyList())
          .forGetter(o -> o.playerConditions),
      ItemStack.CODEC.optionalFieldOf("result", ItemStack.EMPTY).forGetter(o -> o.result),
      ItemStack.CODEC.listOf().optionalFieldOf("results", Collections.emptyList()).forGetter(o -> o.results),
      ChanceOutput.LIST_CODEC.optionalFieldOf("chanceOutputs", Collections.emptyList()).forGetter(o -> o.chanceOutputs),
      Unlock.LIST_CODEC.optionalFieldOf("unlocks", Collections.emptyList()).forGetter(o -> o.unlocks)
  ).apply(instance, BaseRecipeData::new));
  public static Codec<BaseRecipeData> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC.codec())
      .xmap(o -> o.orElse(new BaseRecipeData()), o -> o == null ? Optional.empty() : Optional.of(o));
  public static StreamCodec<RegistryFriendlyByteBuf, NonNullList<Ingredient>> INGREDIENT_LIST_STREAM = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));
  public static final StreamCodec<RegistryFriendlyByteBuf, BaseRecipeData> STREAM_CODEC = ExtraStreamCodecs.composite(
      ByteBufCodecs.optional(INGREDIENT_LIST_STREAM), o -> c(o.ingredients),
      ByteBufCodecs.optional(LevelCondition.LIST_STREAM_CODEC), o -> c(o.levelConditions),
      ByteBufCodecs.optional(PlayerCondition.LIST_STREAM_CODEC), o -> c(o.playerConditions),
      ByteBufCodecs.optional(ItemStack.STREAM_CODEC), o -> o.result == null || o.result.isEmpty() ? Optional.empty() : Optional.of(o.result),
      ByteBufCodecs.optional(ItemStack.LIST_STREAM_CODEC), o -> c(o.results),
      ByteBufCodecs.optional(ChanceOutput.LIST_STREAM_CODEC), o -> c(o.chanceOutputs),
      ByteBufCodecs.optional(Unlock.LIST_STREAM_CODEC), o -> c(o.unlocks),
      BaseRecipeData::new
  );

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
    this.levelConditions = Collections.unmodifiableList(levelConditions);
    this.playerConditions = Collections.unmodifiableList(playerConditions);
    this.result = result;
    this.results = Collections.unmodifiableList(results);
    this.chanceOutputs = Collections.unmodifiableList(chanceOutputs);
    this.unlocks = Collections.unmodifiableList(unlocks);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private BaseRecipeData(Optional<NonNullList<Ingredient>> ingredients, Optional<List<LevelCondition>> levelConditions, Optional<List<PlayerCondition>> playerConditions, Optional<ItemStack> itemStack, Optional<List<ItemStack>> itemStacks, Optional<List<ChanceOutput>> chanceOutputs, Optional<List<Unlock<?>>> unlocks) {
    this(ingredients.orElse(NonNullList.create()), levelConditions.orElse(new ArrayList<>()), playerConditions.orElse(new ArrayList<>()), itemStack.orElse(ItemStack.EMPTY), itemStacks.orElse(new ArrayList<>()), chanceOutputs.orElse(new ArrayList<>()), unlocks.orElse(new ArrayList<>()));
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

  public boolean isEmpty() {
    return ingredients.isEmpty() && levelConditions.isEmpty() && playerConditions.isEmpty() && result.isEmpty() && results.isEmpty() && chanceOutputs.isEmpty() && unlocks.isEmpty();
  }

  // TODO: Why does this exist
  public Builder builder() {
    return new Builder(new ArrayList<>(ingredients), new ArrayList<>(levelConditions), new ArrayList<>(playerConditions), result.copy(), results.stream()
        .map(ItemStack::copy).collect(Collectors.toList()), chanceOutputs.stream().map(ChanceOutput::copy)
        .collect(Collectors.toList()), new ArrayList<>(unlocks));
  }

  private static <V, T extends List<V>> Optional<T> c(T value) {
    return value == null || value.isEmpty() ? Optional.empty() : Optional.of(value);
  }

  public static class Builder {
    private final List<Ingredient> ingredients;
    private final List<LevelCondition> levelConditions;
    private final List<PlayerCondition> playerConditions;
    private ItemStack result;
    private final List<ItemStack> results;
    private final List<ChanceOutput> chanceOutputs;
    private final List<Unlock<?>> unlocks;

    protected Builder(List<Ingredient> ingredients, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, ItemStack result, List<ItemStack> results, List<ChanceOutput> chanceOutputs, List<Unlock<?>> unlocks) {
      this.ingredients = ingredients;
      this.levelConditions = levelConditions;
      this.playerConditions = playerConditions;
      this.result = result;
      this.results = results;
      this.chanceOutputs = chanceOutputs;
      this.unlocks = unlocks;
    }

    protected Builder() {
      this.ingredients = new ArrayList<>();
      this.levelConditions = new ArrayList<>();
      this.playerConditions = new ArrayList<>();
      this.result = ItemStack.EMPTY;
      this.results = new ArrayList<>();
      this.chanceOutputs = new ArrayList<>();
      this.unlocks = new ArrayList<>();
    }

    public Builder requires(Ingredient ingredient) {
      this.ingredients.add(ingredient);
      return this;
    }

    public Builder requires(Holder<? extends ItemLike> item) {
      return requires(item.value().asItem());
    }

    public Builder requires(Item item) {
      return requires(Ingredient.of(item));
    }

    public Builder requires(TagKey<Item> tag) {
      return requires(Ingredient.of(tag));
    }

    public Builder result(Holder<? extends ItemLike> item) {
      return result(new ItemStack(item.value().asItem()));
    }

    public Builder result(Holder<? extends ItemLike> item, int count) {
      return result(new ItemStack(item.value().asItem(), count));
    }

    public Builder result(ItemStack result) {
      this.result = result;
      return this;
    }

    public Builder multiResult(ItemStack result) {
      this.results.add(result);
      return this;
    }

    public Builder multiResult(Holder<? extends ItemLike> holder) {
      return multiResult(new ItemStack(holder.value().asItem()));
    }

    public Builder multiResult(Holder<? extends ItemLike> holder, int count) {
      return multiResult(new ItemStack(holder.value().asItem(), count));
    }

    public Builder chanceOutput(ChanceOutput chanceOutput) {
      this.chanceOutputs.add(chanceOutput);
      return this;
    }

    public Builder chanceOutput(ItemStack itemStack, float chance) {
      this.chanceOutputs.add(new ChanceOutput(itemStack, chance));
      return this;
    }

    public Builder chanceOutput (Holder<? extends ItemLike> holder, float chance) {
      return chanceOutput(new ItemStack(holder.value().asItem()), chance);
    }

    public Builder chanceOutput (Holder<? extends ItemLike> holder, int count, float chance) {
      return chanceOutput(new ItemStack(holder.value().asItem(), count), chance);
    }

    public Builder unlocks(Unlock<?> unlock) {
      this.unlocks.add(unlock);
      return this;
    }

    public Builder condition(LevelCondition levelCondition) {
      this.levelConditions.add(levelCondition);
      return this;
    }

    public Builder condition(PlayerCondition playerCondition) {
      this.playerConditions.add(playerCondition);
      return this;
    }

    public Builder multiplty(int value) {
      List<Ingredient> newIngredients = new ArrayList<>();
      List<ItemStack> newResults = new ArrayList<>();
      List<ChanceOutput> newChances = new ArrayList<>();
      for (int i = 0; i < value; i++) {
        newIngredients.addAll(ingredients);
        for (ItemStack oldResults : results) {
          ItemStack newResult = oldResults.copy();
          newResult.setCount(newResult.getCount() * value); // TODO: Handle overflow
          newResults.add(newResult);
        }
        for (ChanceOutput oldChance : chanceOutputs) {
          newChances.add(oldChance.multiply(value));
        }
      }
      ItemStack newResult = result.copy();
      newResult.setCount(newResult.getCount() * value);
      return new Builder(newIngredients, new ArrayList<>(levelConditions), new ArrayList<>(playerConditions), newResult, newResults, newChances, new ArrayList<>(unlocks));
    }

    public BaseRecipeData build() {
      return new BaseRecipeData(NonNullList.copyOf(ingredients), levelConditions, playerConditions, result, results, chanceOutputs, unlocks);
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
