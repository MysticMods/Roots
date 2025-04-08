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
      ChanceOutput.LIST_CODEC.optionalFieldOf("chanceOutputs", Collections.emptyList()).forGetter(o -> o.chanceOutputs),
      Unlock.LIST_CODEC.optionalFieldOf("unlocks", Collections.emptyList()).forGetter(o -> o.unlocks),
      Codec.INT.fieldOf("priority").forGetter(o -> o.priority)
  ).apply(instance, BaseRecipeData::new));
  public static Codec<BaseRecipeData> OPTIONAL_CODEC = ExtraCodecs.optionalEmptyMap(CODEC.codec())
      .xmap(o -> o.orElse(new BaseRecipeData()), o -> o == null ? Optional.empty() : Optional.of(o));
  public static StreamCodec<RegistryFriendlyByteBuf, NonNullList<Ingredient>> INGREDIENT_LIST_STREAM = Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.collection(NonNullList::createWithCapacity));
  public static final StreamCodec<RegistryFriendlyByteBuf, BaseRecipeData> STREAM_CODEC = ExtraStreamCodecs.composite(
      ByteBufCodecs.optional(INGREDIENT_LIST_STREAM), o -> c(o.ingredients),
      ByteBufCodecs.optional(LevelCondition.LIST_STREAM_CODEC), o -> c(o.levelConditions),
      ByteBufCodecs.optional(PlayerCondition.LIST_STREAM_CODEC), o -> c(o.playerConditions),
      ByteBufCodecs.optional(ItemStack.STREAM_CODEC), o -> o.result == null || o.result.isEmpty() ? Optional.empty() : Optional.of(o.result),
      ByteBufCodecs.optional(ChanceOutput.LIST_STREAM_CODEC), o -> c(o.chanceOutputs),
      ByteBufCodecs.optional(Unlock.LIST_STREAM_CODEC), o -> c(o.unlocks),
      ByteBufCodecs.VAR_INT, o -> o.priority,
      BaseRecipeData::new
  );

  public NonNullList<Ingredient> ingredients;
  public List<LevelCondition> levelConditions;
  public List<PlayerCondition> playerConditions;
  public ItemStack result;
  public List<ChanceOutput> chanceOutputs;
  public List<Unlock<?>> unlocks;
  public int priority;

  public BaseRecipeData() {
  }

  public BaseRecipeData(NonNullList<Ingredient> ingredients, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, ItemStack result, List<ChanceOutput> chanceOutputs, List<Unlock<?>> unlocks, int priority) {
    this.ingredients = ingredients;
    this.levelConditions = Collections.unmodifiableList(levelConditions);
    this.playerConditions = Collections.unmodifiableList(playerConditions);
    this.result = result;
    this.chanceOutputs = Collections.unmodifiableList(chanceOutputs);
    this.unlocks = Collections.unmodifiableList(unlocks);
    this.priority = priority;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private BaseRecipeData(Optional<NonNullList<Ingredient>> ingredients, Optional<List<LevelCondition>> levelConditions, Optional<List<PlayerCondition>> playerConditions, Optional<ItemStack> itemStack, Optional<List<ChanceOutput>> chanceOutputs, Optional<List<Unlock<?>>> unlocks, int priority) {
    this(ingredients.orElse(NonNullList.create()), levelConditions.orElse(new ArrayList<>()), playerConditions.orElse(new ArrayList<>()), itemStack.orElse(ItemStack.EMPTY), chanceOutputs.orElse(new ArrayList<>()), unlocks.orElse(new ArrayList<>()), priority);
  }

  public void updateFrom(BaseRecipeData data) {
    this.ingredients = data.ingredients;
    this.levelConditions = data.levelConditions;
    this.playerConditions = data.playerConditions;
    this.result = data.result;
    this.chanceOutputs = data.chanceOutputs;
    this.unlocks = data.unlocks;
    this.priority = data.priority;
  }

  public boolean isEmpty() {
    return ingredients.isEmpty() && levelConditions.isEmpty() && playerConditions.isEmpty() && result.isEmpty() && chanceOutputs.isEmpty() && unlocks.isEmpty();
  }

  private static <V, T extends List<V>> Optional<T> c(T value) {
    return value == null || value.isEmpty() ? Optional.empty() : Optional.of(value);
  }

  public static class Builder {
    private final List<Ingredient> ingredients;
    private final List<LevelCondition> levelConditions;
    private final List<PlayerCondition> playerConditions;
    private ItemStack result;
    private final List<ChanceOutput> chanceOutputs;
    private final List<Unlock<?>> unlocks;
    private int priority;

    protected Builder(List<Ingredient> ingredients, List<LevelCondition> levelConditions, List<PlayerCondition> playerConditions, ItemStack result, List<ChanceOutput> chanceOutputs, List<Unlock<?>> unlocks, int priority) {
      this.ingredients = ingredients;
      this.levelConditions = levelConditions;
      this.playerConditions = playerConditions;
      this.result = result;
      this.chanceOutputs = chanceOutputs;
      this.unlocks = unlocks;
      this.priority = priority;
    }

    protected Builder() {
      this.ingredients = new ArrayList<>();
      this.levelConditions = new ArrayList<>();
      this.playerConditions = new ArrayList<>();
      this.result = ItemStack.EMPTY;
      this.chanceOutputs = new ArrayList<>();
      this.unlocks = new ArrayList<>();
      this.priority = 0;
    }

    public Builder priority(int value) {
      this.priority = value;
      return this;
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
      return result(item.value(), count);
    }

    public Builder result(ItemLike item, int count) {
      return result(new ItemStack(item.asItem(), count));
    }

    public Builder result(ItemStack result) {
      this.result = result;
      return this;
    }

    public Builder chanceOutput(ChanceOutput chanceOutput) {
      this.chanceOutputs.add(chanceOutput);
      return this;
    }

    public Builder chanceOutput(ItemStack itemStack, float chance) {
      this.chanceOutputs.add(new ChanceOutput(itemStack, chance));
      return this;
    }

    public Builder chanceOutput(ItemLike item, float chance) {
      return chanceOutput(new ItemStack(item.asItem()), chance);
    }

    public Builder chanceOutput(ItemLike item, int count, float chance) {
      return chanceOutput(new ItemStack(item.asItem(), count), chance);
    }

    public Builder chanceOutput(Holder<? extends ItemLike> holder, float chance) {
      return chanceOutput(new ItemStack(holder.value().asItem()), chance);
    }

    public Builder chanceOutput(Holder<? extends ItemLike> holder, int count, float chance) {
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
      List<ChanceOutput> newChances = new ArrayList<>();
      for (int i = 0; i < value; i++) {
        newIngredients.addAll(ingredients);
        for (ChanceOutput oldChance : chanceOutputs) {
          newChances.add(oldChance.multiply(value));
        }
      }
      ItemStack newResult = result.copy();
      newResult.setCount(newResult.getCount() * value);
      return new Builder(newIngredients, new ArrayList<>(levelConditions), new ArrayList<>(playerConditions), newResult, newChances, new ArrayList<>(unlocks), priority);
    }

    public BaseRecipeData build() {
      return new BaseRecipeData(NonNullList.copyOf(ingredients), levelConditions, playerConditions, result, chanceOutputs, unlocks, priority);
    }

    public static Builder create() {
      return new Builder();
    }
  }
}
