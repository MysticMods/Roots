package epicsquid.roots.capability.grove;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.CompoundNBT;

public interface IPlayerGroveCapability {

  void addTrust(GroveType type, float amount);

  float getTrust(GroveType type);

  CompoundNBT getData();

  void setData(CompoundNBT tag);

  void markDirty();

  boolean isDirty();

  void clean();

}
