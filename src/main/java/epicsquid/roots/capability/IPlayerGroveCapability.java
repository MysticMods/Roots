package epicsquid.roots.capability;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerGroveCapability {

  public void addTrust(GroveType type, float amount);
  public NBTTagCompound getData();
  public void setData(NBTTagCompound tag);
  public void markDirty();
  public boolean isDirty();
  public void clean();

}
