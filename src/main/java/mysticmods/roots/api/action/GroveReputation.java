package mysticmods.roots.api.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GroveReputation(int gain1, int gain2, int gain3, int gain4) {
  public static final MapCodec<GroveReputation> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.INT.fieldOf("gain1").forGetter(GroveReputation::gain1),
      Codec.INT.fieldOf("gain2").forGetter(GroveReputation::gain2),
      Codec.INT.fieldOf("gain3").forGetter(GroveReputation::gain3),
      Codec.INT.fieldOf("gain4").forGetter(GroveReputation::gain4)
  ).apply(instance, GroveReputation::new));
}
