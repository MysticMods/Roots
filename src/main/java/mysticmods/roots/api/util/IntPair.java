package mysticmods.roots.api.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record IntPair(int key, int value) {
  public static final Codec<IntPair> PAIR_KEY = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("key")
      .forGetter(IntPair::key), Codec.INT.fieldOf("value").forGetter(IntPair::value)).apply(instance, IntPair::new));
  public static final Codec<List<IntPair>> PAIR_KEY_LIST = PAIR_KEY.listOf();
  public static final Codec<Int2IntMap> INT_MAP_CODEC = PAIR_KEY_LIST.xmap(IntPair::toMap, IntPair::fromMap);
  public static final StreamCodec<ByteBuf, IntPair> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, IntPair::key, ByteBufCodecs.VAR_INT, IntPair::value, IntPair::new);
  public static final StreamCodec<ByteBuf, List<IntPair>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());
  public static final StreamCodec<ByteBuf, Int2IntMap> INT_MAP_STREAM_CODEC = LIST_STREAM_CODEC.map(IntPair::toMap, IntPair::fromMap);

  public static List<IntPair> fromMap(Int2IntMap map) {
    return map.int2IntEntrySet().stream().map(o -> new IntPair(o.getIntKey(), o.getIntValue())).toList();
  }

  public static Int2IntMap toMap(List<IntPair> pairs) {
    Int2IntMap result = new Int2IntOpenHashMap(pairs.size());
    for (IntPair pair : pairs) {
      result.put(pair.key, pair.value);
    }
    return result;
  }
}
