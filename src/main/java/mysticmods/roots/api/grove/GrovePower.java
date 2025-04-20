package mysticmods.roots.api.grove;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;

public interface GrovePower {
  int getMaxPower();

  int getUsedPower();

  boolean consumePower(int amount);

  sealed interface Congen permits Generator, Consumer {
    TagKey<Grove> tag();

    int value();

    static <T extends Congen> MapCodec<T> codec(BiFunction<TagKey<Grove>, Integer, T> builder) {
      return RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(RootsRegistries.Keys.GROVES)
              .fieldOf("tag").forGetter(Congen::tag),
          Codec.INT.fieldOf("value").forGetter(Congen::value)).apply(instance, builder));
    }

    static <T extends Congen> StreamCodec<ByteBuf, T> streamCodec(BiFunction<TagKey<Grove>, Integer, T> builder) {
      return StreamCodec.composite(ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), Congen::tag, ByteBufCodecs.VAR_INT, Congen::value, builder);
    }
  }

  record Generator(TagKey<Grove> tag, int value) implements Congen {
    public static final MapCodec<Generator> MAP_CODEC = Congen.codec(Generator::new);
    public static final Codec<Generator> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, Generator> STREAM_CODEC = Congen.streamCodec(Generator::new);
    public static final Codec<List<Generator>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<ByteBuf, List<Generator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    int generate(IGroveInstance grove, BlockPos pos) {
      if (!grove.asGrove().is(tag)) {
        return 0;
      }

      return value * grove.getRank();
    }
  }

  record Consumer(TagKey<Grove> tag, int value) implements Congen {
    public static final MapCodec<Consumer> MAP_CODEC = Congen.codec(Consumer::new);
    public static final Codec<Consumer> CODEC = MAP_CODEC.codec();
    public static final StreamCodec<ByteBuf, Consumer> STREAM_CODEC = Congen.streamCodec(Consumer::new);
    public static final Codec<List<Consumer>> LIST_CODEC = CODEC.listOf();
    public static final StreamCodec<ByteBuf, List<Consumer>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

    boolean consume(IGroveInstance grove, BlockPos pos) {
      return grove.getPower().consumePower(value);
    }
  }

  enum Symmetry {
    NONE,
    REQUIRE_RADIAL,
    REQUIRE_ASYMMETRY;

    @Nullable
    public BlockPos getPaired(BlockPos start, BlockPos center) {
      if (this == NONE) {
        return null;
      }

      return start.subtract(center).multiply(-1).offset(center);
    }
  }
}
