package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record GrovePowerEntityFlag(boolean eligible, boolean producedPower) {
  public static final GrovePowerEntityFlag DEFAULT = new GrovePowerEntityFlag(false, false);

  public static final MapCodec<GrovePowerEntityFlag> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.BOOL.fieldOf("eligible")
      .forGetter(GrovePowerEntityFlag::eligible), Codec.BOOL.fieldOf("producedPower")
      .forGetter(GrovePowerEntityFlag::producedPower)).apply(instance, GrovePowerEntityFlag::new));
  public static final Codec<GrovePowerEntityFlag> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<ByteBuf, GrovePowerEntityFlag> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, GrovePowerEntityFlag::eligible, ByteBufCodecs.BOOL, GrovePowerEntityFlag::producedPower, GrovePowerEntityFlag::new);
}
