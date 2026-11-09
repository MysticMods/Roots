package mysticmods.roots.api.grove.power.consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.grove.Grove;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public record PowerCollection(ImmutableList<PowerRequest> list,
                              int hash) implements Iterable<PowerRequest> {
  public static final PowerCollection EMPTY = new PowerCollection();
  public static final Codec<PowerCollection> LIST_CODEC = PowerRequest.CODEC.listOf()
      .xmap(ImmutableList::copyOf, Function.identity()).xmap(PowerCollection::new, PowerCollection::list);
  public static final StreamCodec<RegistryFriendlyByteBuf, PowerCollection> LIST_STREAM_CODEC = PowerRequest.STREAM_CODEC.apply(ByteBufCodecs.list())
      .map(ImmutableList::copyOf, Function.identity())
      .map(PowerCollection::new, PowerCollection::list);

  public PowerCollection {
    list = ImmutableList.copyOf(list); // cheap no-op if already immutable
    hash = list.hashCode();
  }

  public PowerCollection() {
    this(List.of());
  }

  public PowerCollection (TagKey<Grove> tag, int value) {
    this(new PowerRequest(tag, value));
  }

  public PowerCollection (PowerRequest request) {
    this(ImmutableList.of(request));
  }

  public PowerCollection(List<PowerRequest> list) {
    this(ImmutableList.copyOf(list), 0);
  }

  public int size() {
    return list.size();
  }

  public boolean isEmpty() {
    return list.isEmpty();
  }

  public PowerRequest get (int index) {
    return list.get(index);
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
  public @NotNull Iterator<PowerRequest> iterator() {
    return list.iterator();
  }

  public PowerTicket create (long tick) {
    return new PowerTicket(this, tick);
  }
}
