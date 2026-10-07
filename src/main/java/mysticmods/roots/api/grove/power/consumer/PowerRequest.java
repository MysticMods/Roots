package mysticmods.roots.api.grove.power.consumer;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.grove.power.GrovePowerBase;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;

import java.util.List;

public record PowerRequest(TagKey<Grove> tag, int value) implements GrovePowerBase {
  public static final MapCodec<PowerRequest> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(RootsRegistries.Keys.GROVES)
          .fieldOf("tag").forGetter(PowerRequest::tag),
      Codec.INT.fieldOf("value").forGetter(PowerRequest::value)).apply(instance, PowerRequest::new));
  public static final Codec<PowerRequest> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, PowerRequest> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), PowerRequest::tag, ByteBufCodecs.VAR_INT, PowerRequest::value, PowerRequest::new);
  public static final Codec<List<PowerRequest>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<ByteBuf, List<PowerRequest>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  @Override
  public int value(IGroveInstance instance) {
    return value();
  }
}
