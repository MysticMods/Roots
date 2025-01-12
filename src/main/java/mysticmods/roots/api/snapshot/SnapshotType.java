package mysticmods.roots.api.snapshot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface SnapshotType<S extends Snapshot> {
  Codec<S> codec ();

  MapCodec<S> mapCodec();

  StreamCodec<RegistryFriendlyByteBuf, S> streamCodec ();

  default S cast (Snapshot snapshot) {
    if (snapshot.getType() != this) {
      throw new IllegalArgumentException("Snapshot types do not match: " + snapshot.getType() + " != " + this);
    }
    return (S) snapshot;
  }
}
