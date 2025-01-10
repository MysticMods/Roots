package mysticmods.roots.api.test.entity;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface EntityTestType<P extends EntityTest> {
  MapCodec<P> codec();

  StreamCodec<RegistryFriendlyByteBuf, P> streamCodec();
}
