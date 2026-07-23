package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public record DecayableDropInfo(Item item, float chance, int minimum, int tries) {
  public static final DecayableDropInfo NONE = new DecayableDropInfo(Items.AIR, 0, 0, 0);

  public static final MapCodec<DecayableDropInfo> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item")
          .forGetter(DecayableDropInfo::item), Codec.FLOAT.fieldOf("chance")
          .forGetter(DecayableDropInfo::chance), Codec.INT.fieldOf("tries").forGetter(DecayableDropInfo::tries))
      .apply(instance, DecayableDropInfo::new));
  public static final Codec<DecayableDropInfo> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, DecayableDropInfo> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.registry(Registries.ITEM), DecayableDropInfo::item,
      ByteBufCodecs.FLOAT, DecayableDropInfo::chance,
      ByteBufCodecs.VAR_INT, DecayableDropInfo::tries,
      DecayableDropInfo::new
  );

  public DecayableDropInfo(ItemStack item, float chance, int minimum, int tries) {
    this(item.getItem(), chance, minimum, tries);
  }

  public DecayableDropInfo(Item item, float chance, int tries) {
    this(item, chance, 1, tries);
  }

  public ItemStack run(RandomSource random) {
    if (chance <= 0 || tries <= 0 || item == Items.AIR) {
      return ItemStack.EMPTY;
    }
    ItemStack result = new ItemStack(item);
    int count = 0;
    for (int i = 0; i < tries; i++) {
      if (random.nextFloat() < chance) {
        count++;
      }
    }
    if (minimum == 0 && count == 0) {
      return ItemStack.EMPTY;
    }
    result.setCount(Math.max(minimum, count));
    return result;
  }
}
