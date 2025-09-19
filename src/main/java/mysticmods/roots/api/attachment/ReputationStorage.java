package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.action.UniqueReputation;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.ReputationRanks;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.Set;

public class ReputationStorage implements ICleanable {
  public static final MapCodec<ReputationStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.BOOL.fieldOf("untrue_pacifist").forGetter(o -> o.untruePacifist),
      Codec.unboundedMap(RootsRegistries.GROVES.byNameCodec(), Codec.INT).fieldOf("reputations")
          .forGetter(o -> o.reputations),
      UniqueReputation.SET_CODEC.fieldOf("unique_reputations").forGetter(o -> o.uniqueReputations)
  ).apply(instance, ReputationStorage::new));
  public static final Codec<ReputationStorage> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, ReputationStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, o -> o.untruePacifist, ByteBufCodecs.map(Object2IntLinkedOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), ByteBufCodecs.INT), o -> o.reputations, UniqueReputation.SET_STREAM_CODEC, o -> o.uniqueReputations, ReputationStorage::new);
  private boolean untruePacifist = false;

  private final Object2IntLinkedOpenHashMap<Grove> reputations;
  private final ObjectOpenHashSet<UniqueReputation> uniqueReputations;

  private boolean dirty = true;

  public ReputationStorage() {
    reputations = new Object2IntLinkedOpenHashMap<>();
    uniqueReputations = new ObjectOpenHashSet<>();
  }

  public ReputationStorage(boolean untruePacifist, Map<Grove, Integer> reputations, Set<UniqueReputation> uniqueReputations) {
    this.untruePacifist = untruePacifist;
    this.reputations = new Object2IntLinkedOpenHashMap<>(reputations);
    this.uniqueReputations = new ObjectOpenHashSet<>(uniqueReputations);
  }

  public int getRank(Grove grove) {
    return grove.getRanks().getRank(reputations.computeIfAbsent(grove, t -> 0));
  }

  public ReputationRanks.Progress getProgress(Grove grove) {
    return grove.getRanks().getProgress(reputations.computeIfAbsent(grove, t -> 0));
  }

  public int getReputation(Grove grove) {
    return reputations.computeIfAbsent(grove, t -> 0);
  }

  public void setReputation(Grove grove, int reputation) {
    reputations.put(grove, reputation);
    setDirty(true);
  }

  public int apply(Grove grove, ResourceLocation name, GroveReputation reputation) {
    UniqueReputation rep = new UniqueReputation(grove.builtInRegistryHolder().getKey().location(), name);
    if (uniqueReputations.contains(rep)) {
      return 0;
    }
    uniqueReputations.add(rep);
    return increaseReputation(grove, reputation.gain1());
  }

  public int adjust(Grove grove, GroveReputation reputation) {
    int rank = getRank(grove);
    return increaseReputation(grove, reputation.byIndex(rank));
  }

  public int increaseReputation(Grove grove, int reputation) {
    int current = reputations.getOrDefault(grove, 0);
    reputations.put(grove, current + reputation);
    setDirty(true);
    return reputation;
  }

  public int decreaseReputation(Grove grove, int reputation) {
    int current = reputations.computeIfAbsent(grove, t -> 0);
    if (reputation > 0) {
      reputation = -reputation;
    }
    reputations.put(grove, Math.max(0, current + reputation));
    setDirty(true);
    return reputation;
  }

  public boolean markUntruePacifist(boolean value) {
    untruePacifist = value;
    setDirty(true);
    return untruePacifist;
  }

  public boolean isUntruePacifist() {
    return untruePacifist;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }
}
