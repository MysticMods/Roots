package epicsquid.roots.capability;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerGroveCapability {

  void addTrust(GroveType type, float amount);
  float getTrust(GroveType type);
  NBTTagCompound getData();
  void setData(NBTTagCompound tag);
  void markDirty();
  boolean isDirty();
  void clean();

}
