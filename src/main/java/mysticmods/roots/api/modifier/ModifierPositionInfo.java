package mysticmods.roots.api.modifier;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

// Calculated once per ModifierTree
public class ModifierPositionInfo {
  public static final Codec<ModifierPositionInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      Codec.FLOAT.fieldOf("x").forGetter(ModifierPositionInfo::x),
      Codec.FLOAT.fieldOf("y").forGetter(ModifierPositionInfo::y)
  ).apply(instance, ModifierPositionInfo::new));
  public static final StreamCodec<ByteBuf, ModifierPositionInfo> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT, ModifierPositionInfo::x, ByteBufCodecs.FLOAT, ModifierPositionInfo::y, ModifierPositionInfo::new);

  private float x, y;

  public ModifierPositionInfo() {
  }

  protected ModifierPositionInfo(float x, float y) {
    this();
    this.x = x;
    this.y = y;
  }

  public void setLocation(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public float x() {
    return x;
  }

  public float y() {
    return y;
  }
}
