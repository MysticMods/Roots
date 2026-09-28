package mysticmods.roots.api.grove.consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.grove.Congen;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;

import java.util.List;

public record BlockConsumer(TagKey<Grove> tag, int value) implements Congen {
  public static final MapCodec<BlockConsumer> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(RootsRegistries.Keys.GROVES)
          .fieldOf("tag").forGetter(Congen::tag),
      Codec.INT.fieldOf("value").forGetter(Congen::value)).apply(instance, BlockConsumer::new));
  public static final Codec<BlockConsumer> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, BlockConsumer> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), Congen::tag, ByteBufCodecs.VAR_INT, Congen::value, BlockConsumer::new);
  public static final Codec<List<BlockConsumer>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<ByteBuf, List<BlockConsumer>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
}
