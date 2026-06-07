package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public class Cost {
  private final CostType type;
  private final Holder<Herb> herb;
  private final double value;

  public static final Codec<Cost> CODEC = RecordCodecBuilder.create(instance -> instance.group(CostType.CODEC.fieldOf("type")
          .forGetter(Cost::getType), RootsRegistries.HERBS.holderByNameCodec().fieldOf("herb")
          .forGetter(Cost::getHolder), Codec.DOUBLE.fieldOf("defaultValue").forGetter(Cost::getValue))
      .apply(instance, Cost::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, Cost> STREAM_CODEC = StreamCodec.composite(CostType.STREAM_CODEC, Cost::getType, ByteBufCodecs.holderRegistry(RootsRegistries.Keys.HERBS), Cost::getHolder, ByteBufCodecs.DOUBLE, Cost::getValue, Cost::new);

  protected Cost(CostType type, Holder<Herb> herb, double value) {
    this.type = type;
    this.herb = herb;
    this.value = value;
  }

  public CostType getType() {
    return type;
  }

  protected Holder<Herb> getHolder() {
    return herb;
  }

  public Herb getHerb() {
    return herb.value();
  }

  public double getValue() {
    return value;
  }

  public static Cost add(Holder<Herb> herb, double value) {
    return new Cost(CostType.ADDITIVE, herb, value);
  }

  public static Cost mult(Holder<Herb> herb, double value) {
    return new Cost(CostType.MULTIPLICATIVE_BASE, herb, value);
  }

  public static Cost multTotal (Holder<Herb> herb, double value) {
    return new Cost(CostType.MULTIPLICATIVE_TOTAL, herb, value);
  }

  @Override
  public String toString() {
    return "Cost{" +
        "type=" + type +
        ", herb=" + herb +
        ", value=" + value +
        '}';
  }
}
