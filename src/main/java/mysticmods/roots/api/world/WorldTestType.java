package mysticmods.roots.api.world;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface WorldTestType<W extends WorldTest> {
  MapCodec<W> codec();

  StreamCodec<RegistryFriendlyByteBuf, W> streamCodec();
}
