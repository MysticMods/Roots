package mysticmods.roots.api.spell;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.shorts.ShortArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record SpellInstanceData(ShortArrayList data) {
  public static final Codec<SpellInstanceData> CODEC = Codec.SHORT.listOf().xmap(ShortArrayList::new, o -> o)
      .xmap(SpellInstanceData::new, SpellInstanceData::data);
  public static final StreamCodec<ByteBuf, SpellInstanceData> STREAM_CODEC = StreamCodec.composite(
      ByteBufCodecs.SHORT.apply(ByteBufCodecs.list())
          .map(ShortArrayList::new, o -> o), SpellInstanceData::data, SpellInstanceData::new);

  public SpellInstanceData (int size) {
    this(new ShortArrayList());
    this.data.ensureCapacity(size);
    for (int i = 0; i < size; i++) {
      data.add((short) 0);
    }
  }

  public int size () {
    return data.size();
  }

  public boolean has (int index) {
    return index >= 0 && index < data.size();
  }

  public short get (int index) {
    if (index < 0 || index >= data.size()) {
      // Maybe throw an exception instead
      return -1;
    }
    return data.getShort(index);
  }
}
