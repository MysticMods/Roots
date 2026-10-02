package mysticmods.roots.api.grove.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.Congen;
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

public record BlockGenerator(TagKey<Block> blockTag, TagKey<Grove> tag,
                             int value) implements Congen {
  public static final MapCodec<BlockGenerator> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.BLOCK)
          .fieldOf("blockTag").forGetter(BlockGenerator::blockTag), TagKey.codec(RootsRegistries.Keys.GROVES)
          .fieldOf("tag").forGetter(Congen::tag),
      Codec.INT.fieldOf("value").forGetter(Congen::value)).apply(instance, BlockGenerator::new));

  public static final Codec<BlockGenerator> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, BlockGenerator> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.BLOCK_TAG_STREAM_CODEC, BlockGenerator::blockTag, ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), Congen::tag, ByteBufCodecs.VAR_INT, Congen::value, BlockGenerator::new);
  public static final Codec<List<BlockGenerator>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<ByteBuf, List<BlockGenerator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public int generate(IGroveInstance grove, BlockPos pos) {
    if (!grove.asGrove().is(tag)) {
      return 0;
    }

    if (value == Integer.MAX_VALUE) {
      return value;
    }

    if (grove.asGrove().is(RootsTags.Groves.WILD)) {
      return value;
    }

    return value * grove.getRank();
  }
}
