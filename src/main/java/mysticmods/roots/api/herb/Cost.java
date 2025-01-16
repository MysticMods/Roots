package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public class Cost {
  public static final List<Cost> NO_COSTS = List.of();

  private final CostType type;
  private final Holder<Herb> herb;
  private final double value;

  public static final Codec<Cost> CODEC = RecordCodecBuilder.create(instance -> instance.group(CostType.CODEC.fieldOf("type").forGetter(Cost::getType), RootsRegistries.HERBS.holderByNameCodec().fieldOf("herb").forGetter(Cost::getHolder), Codec.DOUBLE.fieldOf("defaultValue").forGetter(Cost::getValue)).apply(instance, Cost::new));
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
    return new Cost(CostType.MULTIPLICATIVE, herb, value);
  }

  public enum CostType implements StringRepresentable {
    ADDITIVE,
    MULTIPLICATIVE;

    public static final Codec<CostType> CODEC = StringRepresentable.fromEnum(CostType::values);
    public static final IntFunction<CostType> BY_ID = ByIdMap.continuous(CostType::ordinal, CostType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, CostType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CostType::ordinal);

    @Override
    public String getSerializedName() {
      return this.toString().toLowerCase(Locale.ROOT);
    }
  }
}
