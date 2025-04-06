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

  public GroveReputation multiply (int amount) {
    return new GroveReputation(gain1 * amount, gain2 * amount, gain3 * amount, gain4 * amount);
  }

  public GroveReputation add (GroveReputation ... others) {
    int g1 = gain1;
    int g2 = gain2;
    int g3 = gain3;
    int g4 = gain4;

    for (GroveReputation other : others) {
      g1 += other.gain1;
      g2 += other.gain2;
      g3 += other.gain3;
      g4 += other.gain4;
    }

    return new GroveReputation(g1, g2, g3, g4);
  }
}
