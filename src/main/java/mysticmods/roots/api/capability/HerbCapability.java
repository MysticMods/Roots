package mysticmods.roots.api.capability;

import it.unimi.dsi.fastutil.objects.Object2DoubleMap;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HerbCapability implements ICapabilityProvider, ICapabilitySerializable<ListTag>, INetworkedCapability<HerbCapability.SerializedHerbRecord> {
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

  @NotNull
  @Override
  public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    return Capabilities.HERB_CAPABILITY.orEmpty(cap, LazyOptional.of(() -> this));
  }

  @Override
  public ListTag serializeNBT() {
    ListTag result = new ListTag();
    HERB_MAP.forEach((herb, value) -> {
      CompoundTag tag = new CompoundTag();
      tag.putString("herb", Registries.HERB_REGISTRY.get().getKey(herb).toString());
      tag.putDouble("value", value);
      result.add(tag);
    });
    return result;
  }

  @Override
  public void deserializeNBT(ListTag nbt) {
    HERB_MAP.clear();
    for (int i = 0; i < nbt.size(); i++) {
      CompoundTag tag = nbt.getCompound(i);
      HERB_MAP.put(Registries.HERB_REGISTRY.get().getValue(new ResourceLocation(tag.getString("herb"))), tag.getDouble("value"));
    }
    setDirty(true);
  }


  @Override
  public void fromRecord(SerializedHerbRecord record) {
    HERB_MAP.clear();
    HERB_MAP.putAll(record.getHerbMap());
    setDirty(true);
  }

  @Override
  public SerializedHerbRecord toRecord() {
    return new SerializedHerbRecord(HERB_MAP);
  }

  @Override
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  public static SerializedHerbRecord fromNetwork (FriendlyByteBuf buf) {
    SerializedHerbRecord result = new SerializedHerbRecord();
    result.fromNetwork(buf);
    return result;
  }

  public static class SerializedHerbRecord implements SerializedCapability {
    private final Object2DoubleOpenHashMap<Herb> HERB_MAP = new Object2DoubleOpenHashMap<>();

    public SerializedHerbRecord() {
      HERB_MAP.defaultReturnValue(0.0d);
    }

    public SerializedHerbRecord (Object2DoubleOpenHashMap<Herb> herbMap) {
      this();
      this.HERB_MAP.putAll(herbMap);
    }

    @Override
    public void fromNetwork(FriendlyByteBuf buf) {
      HERB_MAP.clear();
      int mapSize = buf.readVarInt();
      for (int i = 0; i < mapSize; i++) {
        HERB_MAP.put(Registries.HERB_REGISTRY.get().getValue(buf.readVarInt()), buf.readDouble());
      }
    }

    @Override
    public void toNetwork(FriendlyByteBuf buf) {
      buf.writeVarInt(HERB_MAP.size());
      for (Object2DoubleMap.Entry<Herb> entry : HERB_MAP.object2DoubleEntrySet()) {
        buf.writeVarInt(Registries.HERB_REGISTRY.get().getID(entry.getKey()));
        buf.writeDouble(entry.getDoubleValue());
      }
    }

    public Object2DoubleOpenHashMap<Herb> getHerbMap() {
      return HERB_MAP;
    }
  }

}
