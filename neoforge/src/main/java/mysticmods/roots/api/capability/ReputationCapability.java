package mysticmods.roots.api.capability;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ReputationCapability {
  private boolean untruePacifist = false;

  private final Object2IntMap<Grove> REPUTATIONS = new Object2IntLinkedOpenHashMap<>();

  private boolean dirty = false;

  public ReputationCapability() {
  }

  public int getReputation (Grove grove) {
    return REPUTATIONS.computeIfAbsent(grove, t -> 0);
  }

  public int setReputation (Grove grove, int reputation) {
    int result = REPUTATIONS.put(grove, reputation);
    setDirty(true);
    return result;
  }

  public int increaseReputation (Grove grove, int reputation) {
    int current = REPUTATIONS.computeIfAbsent(grove, t -> 0);
    int result = REPUTATIONS.put(grove, current + reputation);
    setDirty(true);
    return result;
  }

  public int decreaseReputation (Grove grove, int reputation) {
    int current = REPUTATIONS.computeIfAbsent(grove, t -> 0);
    int result = REPUTATIONS.put(grove, Math.max(0, current - reputation));
    setDirty(true);
    return result;
  }

  public boolean markUntruePacifist (boolean value) {
    untruePacifist = value;
    setDirty(true);
    return untruePacifist;
  }

  public SerializedReputationRecord toRecord() {
    return new SerializedReputationRecord(untruePacifist, REPUTATIONS);
  }

  public void fromRecord (SerializedReputationRecord record) {
    this.untruePacifist = record.getUntruePacifist();
    this.REPUTATIONS.clear();
    this.REPUTATIONS.putAll(record.getReputations());
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }

  public CompoundTag serializeNBT() {
    CompoundTag result = new CompoundTag();
    result.putBoolean("untrue_pacifist", untruePacifist);
    CompoundTag reputations = new CompoundTag();
    REPUTATIONS.forEach((grove, reputation) -> {
      reputations.putInt(String.valueOf(grove.getKey()), reputation);
    });
    result.put("reputations", reputations);
    return result;
  }

  public void deserializeNBT(CompoundTag nbt) {
    REPUTATIONS.clear();
    this.untruePacifist = nbt.getBoolean("untrue_pacifist");
    CompoundTag reputations = nbt.getCompound("reputations");
    reputations.getAllKeys().forEach(key -> {
      ResourceLocation groveKey = ResourceLocation.parse(key);
      Grove grove = RootsRegistries.GROVES.get(groveKey);
      if (grove != null) {
        REPUTATIONS.put(grove, reputations.getInt(key));
      }
    });
    setDirty(true);
  }

  public static SerializedReputationRecord fromNetwork(FriendlyByteBuf buf) {
    SerializedReputationRecord result = new SerializedReputationRecord();
    result.fromNetwork(buf);
    return result;
  }

  public static class SerializedReputationRecord {
    private boolean untruePacifist = false;
    private final Object2IntMap<Grove> reputations = new Object2IntLinkedOpenHashMap<>();

    public SerializedReputationRecord(boolean untruePacifist, Object2IntMap<Grove> reputations) {
      this.reputations.clear();
      this.reputations.putAll(reputations);
      this.untruePacifist = untruePacifist;
    }

    public SerializedReputationRecord() {
    }

    public boolean getUntruePacifist() {
      return untruePacifist;
    }

    public Object2IntMap<Grove> getReputations() {
      return reputations;
    }

    public void fromNetwork(FriendlyByteBuf buf) {
      this.untruePacifist = buf.readBoolean();
      this.reputations.clear();
      int size = buf.readVarInt();
      for (int i = 0; i < size; i++) {
        ResourceLocation groveKey = buf.readResourceLocation();
        Grove grove = RootsRegistries.GROVES.get(groveKey);
        int amount = buf.readVarInt();
        if (grove != null) {
          reputations.put(grove, amount);
        }
      }
    }

    public void toNetwork(FriendlyByteBuf buf) {
      buf.writeBoolean(this.untruePacifist);
      buf.writeVarInt(this.reputations.size());
      for (Object2IntMap.Entry<Grove> entry : this.reputations.object2IntEntrySet()) {
        buf.writeResourceLocation(entry.getKey().getKey());
        buf.writeVarInt(entry.getIntValue());
      }
    }
  }
}
