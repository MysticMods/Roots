package mysticmods.roots.api.grove.power.consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public record PowerCollection(ImmutableList<PowerAccumulationRequest> list,
                              int hash) implements Iterable<PowerAccumulationRequest> {
  public static final PowerCollection EMPTY = new PowerCollection();
  public static final Codec<PowerCollection> LIST_CODEC = PowerAccumulationRequest.CODEC.listOf()
      .xmap(ImmutableList::copyOf, Function.identity()).xmap(PowerCollection::new, PowerCollection::list);
  public static final StreamCodec<RegistryFriendlyByteBuf, PowerCollection> LIST_STREAM_CODEC = PowerAccumulationRequest.STREAM_CODEC.apply(ByteBufCodecs.list())
      .map(ImmutableList::copyOf, Function.identity())
      .map(PowerCollection::new, PowerCollection::list);

  public PowerCollection {
    list = ImmutableList.copyOf(list); // cheap no-op if already immutable
    hash = list.hashCode();
  }

  public PowerCollection() {
    this(List.of());
  }

  public PowerCollection(List<PowerAccumulationRequest> list) {
    this(ImmutableList.copyOf(list), 0);
  }

  public int size() {
    return list.size();
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    return o instanceof PowerCollection(var otherList, int otherHash)
        && hash == otherHash
        && list.equals(otherList);
  }

  @Override
  public int hashCode() {
    return hash;
  }

  @Override
  public @NotNull Iterator<PowerAccumulationRequest> iterator() {
    return list.iterator();
  }
}
