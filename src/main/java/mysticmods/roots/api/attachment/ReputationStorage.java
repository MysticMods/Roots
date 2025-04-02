package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import mysticmods.roots.api.action.GroveReputation;
import mysticmods.roots.api.datamap.GroveReputationEntry;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;

public class ReputationStorage implements ICleanable {
  private static final int RANK_THRESHOLD = 5000;

  public static final MapCodec<ReputationStorage> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
      Codec.BOOL.fieldOf("untrue_pacifist").forGetter(o -> o.untruePacifist),
      Codec.unboundedMap(RootsRegistries.GROVES.byNameCodec(), Codec.INT).fieldOf("reputations")
          .forGetter(o -> o.reputations)
  ).apply(instance, ReputationStorage::new));
  public static final Codec<ReputationStorage> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, ReputationStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.BOOL, o -> o.untruePacifist, ByteBufCodecs.map(Object2IntLinkedOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), ByteBufCodecs.INT), o -> o.reputations, ReputationStorage::new);
  private boolean untruePacifist = false;

  private final Object2IntLinkedOpenHashMap<Grove> reputations;

  private boolean dirty = true;

  public ReputationStorage() {
    reputations = new Object2IntLinkedOpenHashMap<>();
  }

  public ReputationStorage(boolean untruePacifist, Map<Grove, Integer> reputations) {
    this.untruePacifist = untruePacifist;
    this.reputations = new Object2IntLinkedOpenHashMap<>(reputations);
  }

  public int getReputation(Grove grove) {
    return reputations.computeIfAbsent(grove, t -> 0);
  }

  public int setReputation(Grove grove, int reputation) {
    int result = reputations.put(grove, reputation);
    setDirty(true);
    return result;
  }

  public int adjust (Grove grove, GroveReputation reputation) {
    int[] reps = {reputation.gain1(), reputation.gain2(), reputation.gain3(), reputation.gain4()};
    int current = reputations.getOrDefault(grove, 0);
    int rank = Math.min(Math.max((current / RANK_THRESHOLD) - 1, 0), reps.length);
    current = reputations.put(grove, current + reps[rank]);
    setDirty(true);
    return current;
  }

  public int increaseReputation(Grove grove, int reputation) {
    int current = reputations.computeIfAbsent(grove, t -> 0);
    int result = reputations.put(grove, current + reputation);
    setDirty(true);
    return result;
  }

  public int decreaseReputation(Grove grove, int reputation) {
    int current = reputations.computeIfAbsent(grove, t -> 0);
    int result = reputations.put(grove, Math.max(0, current - reputation));
    setDirty(true);
    return result;
  }

  public boolean markUntruePacifist(boolean value) {
    untruePacifist = value;
    setDirty(true);
    return untruePacifist;
  }

  public boolean getUntruePacifist() {
    return untruePacifist;
  }

  @Override
  public boolean isEmpty() {
    return false;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }
}
