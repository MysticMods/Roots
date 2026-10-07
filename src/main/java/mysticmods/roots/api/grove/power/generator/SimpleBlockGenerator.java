package mysticmods.roots.api.grove.power.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.power.GrovePowerBase;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;

public record SimpleBlockGenerator(TagKey<Block> blockTag, TagKey<Grove> tag,
                                   int value) implements GrovePowerBase {
  public static final MapCodec<SimpleBlockGenerator> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.BLOCK)
          .fieldOf("blockTag").forGetter(SimpleBlockGenerator::blockTag), TagKey.codec(RootsRegistries.Keys.GROVES)
          .fieldOf("tag").forGetter(SimpleBlockGenerator::tag),
      Codec.INT.fieldOf("value").forGetter(SimpleBlockGenerator::value)).apply(instance, SimpleBlockGenerator::new));

  public static final Codec<SimpleBlockGenerator> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, SimpleBlockGenerator> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, SimpleBlockGenerator::blockTag, ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), SimpleBlockGenerator::tag, ByteBufCodecs.VAR_INT, SimpleBlockGenerator::value, SimpleBlockGenerator::new);
  public static final Codec<List<SimpleBlockGenerator>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<ByteBuf, List<SimpleBlockGenerator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public int generate(IGroveInstance grove, BlockPos pos) {
    return value(grove);
  }

  @Override
  public int value(IGroveInstance instance) {
    if (!instance.is(tag)) {
      return 0;
    }

    if (value == Integer.MAX_VALUE) {
      return value;
    }

    if (instance.is(RootsTags.Groves.WILD)) {
      return value;
    }

    return value * instance.getRank();
  }
}
