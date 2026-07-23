package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record DecayableDropInfo(ItemStack item, float chance, int tries) {
  public static final DecayableDropInfo NONE = new DecayableDropInfo(ItemStack.EMPTY, 0, 0);

  public static final MapCodec<DecayableDropInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(ItemStack.CODEC.fieldOf("item")
          .forGetter(DecayableDropInfo::item), Codec.FLOAT.fieldOf("chance")
          .forGetter(DecayableDropInfo::chance), Codec.INT.fieldOf("tries").forGetter(DecayableDropInfo::tries))
      .apply(instance, DecayableDropInfo::new));
  public static final Codec<DecayableDropInfo> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, DecayableDropInfo> STREAM_CODEC = StreamCodec.composite(
      ItemStack.STREAM_CODEC, DecayableDropInfo::item,
      ByteBufCodecs.FLOAT, DecayableDropInfo::chance,
      ByteBufCodecs.VAR_INT, DecayableDropInfo::tries,
      DecayableDropInfo::new
  );

  public DecayableDropInfo(Item item, float chance, int tries) {
    this(new ItemStack(item), chance, tries);
  }

  @Nullable
  public ItemStack run(RandomSource random) {
    if (chance <= 0 || tries <= 0 || item.isEmpty()) {
      return null;
    }
    ItemStack result = item.copy();
    int count = 0;
    for (int i = 0; i < tries; i++) {
      if (random.nextFloat() < chance) {
        count++;
      }
    }
    result.setCount(Math.max(1, count));
    return result;
  }
}
