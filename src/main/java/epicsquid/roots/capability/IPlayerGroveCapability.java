package epicsquid.roots.capability;

import java.util.Map;

import epicsquid.roots.grove.GroveType;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerGroveCapability {

  public NBTTagCompound getData();
  public void setData(NBTTagCompound tag);

}
