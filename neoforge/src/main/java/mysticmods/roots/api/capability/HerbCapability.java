package mysticmods.roots.api.capability;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class HerbCapability {
  private boolean dirty = true;
  private final Object2DoubleOpenHashMap<Herb> HERB_MAP = new Object2DoubleOpenHashMap<>();

  public HerbCapability() {
    HERB_MAP.defaultReturnValue(0.0d);
  }

  // Returns how much is left over
  public double drain(Herb herb, double value, boolean simulate) {
    double current = HERB_MAP.getDouble(herb);
    double remainder;
    if (current < value) {
      remainder = value - current;
      if (!simulate) {
        HERB_MAP.put(herb, 0.0d);
        setDirty(true);
        /*        RootsAPI.LOG.info("Drained herb {} x{}, bringing it from {} to 0, with a remainder of {} still required.", herb, value, current, remainder);*/
      }
      return remainder;
    } else {
      if (!simulate) {
        setDirty(true);
        HERB_MAP.put(herb, current - value);
        /*        RootsAPI.LOG.info("Drained herb {} x{}, bringing it from {} to {}", herb, value, current, current - value);*/
      }
      return 0.0d;
    }
  }

  public void fill(Herb herb, double value) {
    double oldValue = HERB_MAP.getDouble(herb);
    double newValue = oldValue + value;
    HERB_MAP.put(herb, newValue);
    RootsAPI.LOG.info("Filled herb {} x{}, bringing it from {} to {}.", herb, value, oldValue, newValue);
    setDirty(true);
  }

  public ListTag serializeNBT() {
    ListTag result = new ListTag();
    HERB_MAP.forEach((herb, value) -> {
      CompoundTag tag = new CompoundTag();
      tag.putString("herb", RootsRegistries.HERBS.getKey(herb).toString());
      tag.putDouble("value", value);
      result.add(tag);
    });
    return result;
  }

  public void deserializeNBT(ListTag nbt) {
    HERB_MAP.clear();
    for (int i = 0; i < nbt.size(); i++) {
      CompoundTag tag = nbt.getCompound(i);
      HERB_MAP.put(RootsRegistries.HERBS.get(ResourceLocation.parse(tag.getString("herb"))), tag.getDouble("value"));
    }
    setDirty(true);
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }

  public static SerializedHerbRecord fromNetwork(FriendlyByteBuf buf) {
    SerializedHerbRecord result = new SerializedHerbRecord();
    result.fromNetwork(buf);
    return result;
  }

  public static class SerializedHerbRecord {
    private final Object2DoubleOpenHashMap<Herb> HERB_MAP = new Object2DoubleOpenHashMap<>();

    public SerializedHerbRecord() {
      HERB_MAP.defaultReturnValue(0.0d);
    }

    public SerializedHerbRecord(Object2DoubleOpenHashMap<Herb> herbMap) {
      this();
      this.HERB_MAP.putAll(herbMap);
    }

    public void fromNetwork(FriendlyByteBuf buf) {
      HERB_MAP.clear();
      int mapSize = buf.readVarInt();
      for (int i = 0; i < mapSize; i++) {
        HERB_MAP.put(RootsRegistries.HERBS.byId(buf.readVarInt()), buf.readDouble());
      }
    }

    public void toNetwork(FriendlyByteBuf buf) {
      buf.writeVarInt(HERB_MAP.size());
      for (Object2DoubleMap.Entry<Herb> entry : HERB_MAP.object2DoubleEntrySet()) {
        buf.writeVarInt(RootsRegistries.HERBS.getId(entry.getKey()));
        buf.writeDouble(entry.getDoubleValue());
      }
    }

    public Object2DoubleOpenHashMap<Herb> getHerbMap() {
      return HERB_MAP;
    }
  }

}
