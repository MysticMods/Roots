package mysticmods.roots.api.recipe;

import com.mojang.datafixers.util.Function7;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
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

import java.util.AbstractList;
import java.util.List;
import java.util.function.Function;

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

  public static StreamCodec<RegistryFriendlyByteBuf, NonNullList<Ingredient>> INGREDIENT_LIST_STREAM = Ingredient.CONTENTS_STREAM_CODEC.apply(nonnullList(Ingredient.EMPTY));

  public static <B extends ByteBuf, V> StreamCodec.CodecOperation<B, V, NonNullList<V>> nonnullList(V defaultValue) {
    return arg -> nonnullList(NonNullList::withSize, defaultValue, arg);
  }

  public interface IntDefaultFunction<C, V> {
    C apply(int i, V defaultValue);
  }

  public static <B extends ByteBuf, V, C extends AbstractList<V>> StreamCodec<B, C> nonnullList(IntDefaultFunction<C, V> intFunction, V defaultValue, StreamCodec<? super B, V> arg) {
    return nonnullList(intFunction, arg, defaultValue, Integer.MAX_VALUE);
  }

  public static <B extends ByteBuf, V, C extends AbstractList<V>> StreamCodec<B, C> nonnullList(IntDefaultFunction<C, V> intFunction, StreamCodec<? super B, V> arg, V defaultValue, int maxValue) {
    return new StreamCodec<B, C>() {
      public C decode(B object) {
        int size = ByteBufCodecs.readCount(object, maxValue);
        C list = intFunction.apply(size, defaultValue);
        for (int i = 0; i < size; i++) {
          list.set(i, arg.decode(object));
        }
        return list;
      }

      public void encode(B object, C list) {
        ByteBufCodecs.writeCount(object, list.size(), maxValue);
        for (V v : list) {
          arg.encode(object, v);
        }
      }
    };
  }


  public static final StreamCodec<RegistryFriendlyByteBuf, BaseRecipeData> STREAM_CODEC = composite(
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

  public static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
      final StreamCodec<? super B, T1> arg, final Function<C, T1> function,
      final StreamCodec<? super B, T2> arg2, final Function<C, T2> function2,
      final StreamCodec<? super B, T3> arg3, final Function<C, T3> function3,
      final StreamCodec<? super B, T4> arg4, final Function<C, T4> function4,
      final StreamCodec<? super B, T5> arg5, final Function<C, T5> function5,
      final StreamCodec<? super B, T6> arg6, final Function<C, T6> function6,
      final StreamCodec<? super B, T7> arg7, final Function<C, T7> function7,
      final Function7<T1, T2, T3, T4, T5, T6, T7, C> function72) {
    return new StreamCodec<B, C>() {

      public C decode(B object) {
        Object object2 = arg.decode(object);
        Object object3 = arg2.decode(object);
        Object object4 = arg3.decode(object);
        Object object5 = arg4.decode(object);
        Object object6 = arg5.decode(object);
        Object object7 = arg6.decode(object);
        Object object8 = arg7.decode(object);
        return function72.apply((T1) object2, (T2) object3, (T3) object4, (T4) object5, (T5) object6, (T6) object7, (T7) object8);
      }

      public void encode(B object, C object2) {
        arg.encode(object, function.apply(object2));
        arg2.encode(object, function2.apply(object2));
        arg3.encode(object, function3.apply(object2));
        arg4.encode(object, function4.apply(object2));
        arg5.encode(object, function5.apply(object2));
        arg6.encode(object, function6.apply(object2));
        arg7.encode(object, function7.apply(object2));
      }
    };
  }
}
