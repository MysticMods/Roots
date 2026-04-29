package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.List;
import java.util.Locale;
import java.util.function.IntFunction;

public record CostInstance(ChargeType chargeType, List<Cost> costs) {
  // TODO: Move charge type out of the instance and into the spell itself
  public static final MapCodec<CostInstance> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
          ChargeType.CODEC.fieldOf("charge_type").forGetter(o -> o.chargeType),
          Cost.CODEC.listOf().fieldOf("costs").forGetter(o -> o.costs)).apply(instance, CostInstance::new));
  public static final Codec<CostInstance> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, CostInstance> STREAM_CODEC = StreamCodec.composite(
      ChargeType.STREAM_CODEC, CostInstance::chargeType, Cost.STREAM_CODEC.apply(ByteBufCodecs.list()), CostInstance::costs, CostInstance::new);

  public static CostInstance of(ChargeType chargeType, Cost... costs) {
    return new CostInstance(chargeType, List.of(costs));
  }

  public static CostInstance of(ChargeType chargeType, List<Cost> costs) {
    return new CostInstance(chargeType, costs);
  }

  public static CostInstance of(Holder<Spell> spell, List<Cost> costs) {
    return of(spell.value(), costs);
  }

  public static CostInstance of(Holder<Spell> spell, Cost... costs) {
    return of(spell.value(), List.of(costs));
  }

  public static CostInstance of(Spell spell, List<Cost> costs) {
    return of(spell.getCosts().chargeType, costs);
  }

  public static CostInstance of(Spell spell, Cost... costs) {
    return of(spell.getCosts().chargeType, List.of(costs));
  }

  public enum ChargeType implements StringRepresentable {
    CAST, // Charges per cast
    OPERATION; // Charges amount per operation

    public static final Codec<ChargeType> CODEC = StringRepresentable.fromEnum(ChargeType::values);
    public static final IntFunction<ChargeType> BY_ID = ByIdMap.continuous(ChargeType::ordinal, ChargeType.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, ChargeType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ChargeType::ordinal);

    @Override
    public String getSerializedName() {
      return this.toString().toLowerCase(Locale.ROOT);
    }
  }
}
