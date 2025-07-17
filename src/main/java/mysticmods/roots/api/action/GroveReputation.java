package mysticmods.roots.api.action;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record GroveReputation(int gain1, int gain2, int gain3, int gain4, int gain5) {
  public static final MapCodec<GroveReputation> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.INT.fieldOf("gain1").forGetter(GroveReputation::gain1),
      Codec.INT.fieldOf("gain2").forGetter(GroveReputation::gain2),
      Codec.INT.fieldOf("gain3").forGetter(GroveReputation::gain3),
      Codec.INT.fieldOf("gain4").forGetter(GroveReputation::gain4),
      Codec.INT.fieldOf("gain5").forGetter(GroveReputation::gain5)
  ).apply(instance, GroveReputation::new));

  public GroveReputation (int gain1) {
    this(gain1, 0, 0, 0, 0);
  }

  public GroveReputation multiply(int amount) {
    return new GroveReputation(gain1 * amount, gain2 * amount, gain3 * amount, gain4 * amount, gain5 * amount);
  }

  public GroveReputation add(GroveReputation... others) {
    int g1 = gain1;
    int g2 = gain2;
    int g3 = gain3;
    int g4 = gain4;
    int g5 = gain5;

    for (GroveReputation other : others) {
      g1 += other.gain1;
      g2 += other.gain2;
      g3 += other.gain3;
      g4 += other.gain4;
      g5 += other.gain5;
    }

    return new GroveReputation(g1, g2, g3, g4, g5);
  }

  public int byIndex (int index) {
    return switch (index) {
      case 0 -> gain1;
      case 1 -> gain2;
      case 2 -> gain3;
      case 3 -> gain4;
      case 4 -> gain5;
      default -> throw new IndexOutOfBoundsException("Index must be between 0 and 4, inclusive.");
    };
  }
}
