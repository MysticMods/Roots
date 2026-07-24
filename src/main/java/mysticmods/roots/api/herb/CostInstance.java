package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public record CostInstance(List<Cost> costs) {
  // TODO: I'm not sure this is safe
  public static final CostInstance NONE = new CostInstance(new ArrayList<>());

  public static final MapCodec<CostInstance> MAP_CODEC =
      Cost.CODEC.listOf().fieldOf("costs").xmap(CostInstance::new, CostInstance::costs);
  public static final Codec<CostInstance> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, CostInstance> STREAM_CODEC = Cost.STREAM_CODEC.apply(ByteBufCodecs.list())
      .map(CostInstance::new, CostInstance::costs);

  public static CostInstance add(Holder<Herb> herb, double value) {
    return of(Cost.add(herb, value));
  }

  public static CostInstance mult(Holder<Herb> herb, double value) {
    return of(Cost.mult(herb, value));
  }

  public static CostInstance multTotal (Holder<Herb> herb, double value) {
    return of(Cost.multTotal(herb, value));
  }

  public static CostInstance of(Cost... costs) {
    return new CostInstance(List.of(costs));
  }

  public static CostInstance of(List<Cost> costs) {
    return new CostInstance(costs);
  }
}
