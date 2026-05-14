package mysticmods.roots.api.herb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.List;

public record CostInstance(List<Cost> costs) {
  public static final MapCodec<CostInstance> MAP_CODEC =
      Cost.CODEC.listOf().fieldOf("costs").xmap(CostInstance::new, CostInstance::costs);
  public static final Codec<CostInstance> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, CostInstance> STREAM_CODEC = Cost.STREAM_CODEC.apply(ByteBufCodecs.list())
      .map(CostInstance::new, CostInstance::costs);

  public static CostInstance of(Cost... costs) {
    return new CostInstance(List.of(costs));
  }

  public static CostInstance of(List<Cost> costs) {
    return new CostInstance(costs);
  }
}
