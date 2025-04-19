package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;

import java.util.List;

public interface GrovePower {
  int getMaxPower ();
  int getReservedPower ();
  boolean reservePower (int amount);

  record Generator (TagKey<Grove> tag, int value) {
    public static final MapCodec<Generator> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("tag").forGetter(Generator::tag),
        Codec.INT.fieldOf("value").forGetter(Generator::value)).apply(instance, Generator::new));
    public static final Codec<Generator> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, Generator> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), Generator::tag, ByteBufCodecs.VAR_INT, Generator::value, Generator::new);
    public static final Codec<List<Generator>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<ByteBuf, List<Generator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    int generate (IGroveInstance grove) {
      if (!grove.asGrove().is(tag)) {
        return 0;
      }

      return value * grove.groveRank();
    }
  }

  enum Symmertry {
    NONE,
    REQUIRE_RADIAL,
    REQUIRE_ASYMMETRY;
  }
}
