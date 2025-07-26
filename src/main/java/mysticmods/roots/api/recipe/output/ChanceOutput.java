package mysticmods.roots.api.recipe.output;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record ChanceOutput(ItemStack output, float chance) {
  public static final Codec<ChanceOutput> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ItemStack.STRICT_CODEC.fieldOf("output").forGetter(ChanceOutput::output),
      Codec.FLOAT.fieldOf("chance").forGetter(ChanceOutput::chance)
  ).apply(instance, ChanceOutput::new));
  public static final Codec<List<ChanceOutput>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, ChanceOutput> STREAM_CODEC = StreamCodec.composite(
      ItemStack.STREAM_CODEC, ChanceOutput::output,
      ByteBufCodecs.FLOAT, ChanceOutput::chance,
      ChanceOutput::new
  );
  public static final StreamCodec<RegistryFriendlyByteBuf, List<ChanceOutput>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public ChanceOutput(ItemStack output, float chance) {
    this.output = output;
    this.chance = chance;
    if (this.chance > 1) {
      throw new IllegalArgumentException("Invalid chance for a chance output: " + this.chance);
    }
  }

  @Nonnull
  public ItemStack getResult(RandomSource random) {
    if (random.nextFloat() < this.chance) {
      return this.output.copy();
    }

    return ItemStack.EMPTY;
  }

  public ChanceOutput copy() {
    return new ChanceOutput(output.copy(), chance);
  }

  public ChanceOutput multiply(int value) {
    ItemStack newStack = output.copy();
    newStack.setCount(newStack.getCount() * value);
    return new ChanceOutput(newStack, chance);
  }

  public static List<ItemStack> getOutputs(List<ChanceOutput> chanceOptions, RandomSource random) {
    List<ItemStack> result = new ArrayList<>();
    for (ChanceOutput output : chanceOptions) {
      ItemStack thisResult = output.getResult(random);
      if (thisResult != null) {
        result.add(thisResult);
      }
    }
    return result;
  }
}
