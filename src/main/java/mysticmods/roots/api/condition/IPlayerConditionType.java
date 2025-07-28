package mysticmods.roots.api.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface IPlayerConditionType<T extends IPlayerCondition> {
  Codec<T> codec();

  MapCodec<T> mapCodec();

  StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();
}
