package mysticmods.roots.api.ritual;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

// TODO: Actually use this
public record RitualInstanceData(IntArrayList data) {
  public static final Codec<RitualInstanceData> CODEC = Codec.INT.listOf().xmap(IntArrayList::new, Function.identity())
      .xmap(RitualInstanceData::new, RitualInstanceData::data);
  public static final StreamCodec<ByteBuf, RitualInstanceData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT.apply(ByteBufCodecs.list())
          .map(IntArrayList::new, o -> o), RitualInstanceData::data, RitualInstanceData::new);

  public RitualInstanceData(int size) {
    this(new IntArrayList(size));
    this.data.ensureCapacity(size);
    for (int i = 0; i < size; i++) {
      data.add(0);
    }
  }

  public int size() {
    return data.size();
  }

  public boolean has(int index) {
    return index >= 0 && index < data.size();
  }

  public int get(int index) {
    if (index < 0 || index >= data.size()) {
      // Maybe throw an exception instead
      return -1;
    }
    return data.getInt(index);
  }
}
