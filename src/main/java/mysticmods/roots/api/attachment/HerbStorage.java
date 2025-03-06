package mysticmods.roots.api.attachment;

import com.mojang.serialization.Codec;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herb.Herb;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;

public class HerbStorage implements ICleanable {
  public static final Codec<HerbStorage> CODEC = Codec.unboundedMap(RootsRegistries.HERBS.byNameCodec(), Codec.DOUBLE)
      .xmap(HerbStorage::new, HerbStorage::getHerbMap);
  public static final StreamCodec<RegistryFriendlyByteBuf, HerbStorage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(Object2DoubleOpenHashMap::new, ByteBufCodecs.registry(RootsRegistries.Keys.HERBS), ByteBufCodecs.DOUBLE), HerbStorage::getHerbMap, HerbStorage::new);

  private boolean dirty = true;
  private final Object2DoubleOpenHashMap<Herb> herbMap;

  public HerbStorage() {
    herbMap = new Object2DoubleOpenHashMap<>();
  }

  public HerbStorage(Map<Herb, Double> herbMap) {
    this.herbMap = new Object2DoubleOpenHashMap<>(herbMap);
  }

  public Object2DoubleOpenHashMap<Herb> getHerbMap() {
    return herbMap;
  }

  public double amount (Herb herb) {
    return herbMap.getDouble(herb);
  }

  // Returns how much is left over
  public double drain(Herb herb, double value, boolean simulate) {
    double current = herbMap.getDouble(herb);
    double remainder;
    if (current < value) {
      remainder = value - current;
      if (!simulate) {
        herbMap.put(herb, 0.0d);
        setDirty(true);
        /*        RootsAPI.LOG.info("Drained herb {} x{}, bringing it from {} to 0, with a remainder of {} still required.", herb, defaultValue, current, remainder);*/
      }
      return remainder;
    } else {
      if (!simulate) {
        setDirty(true);
        herbMap.put(herb, current - value);
        /*        RootsAPI.LOG.info("Drained herb {} x{}, bringing it from {} to {}", herb, defaultValue, current, current - defaultValue);*/
      }
      return 0.0d;
    }
  }

  public void fill(Herb herb, double value) {
    double oldValue = herbMap.getDouble(herb);
    double newValue = oldValue + value;
    herbMap.put(herb, newValue);
    setDirty(true);
  }

  @Override
  public boolean isEmpty() {
    return herbMap.isEmpty();
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public boolean isDirty() {
    return dirty;
  }
}
