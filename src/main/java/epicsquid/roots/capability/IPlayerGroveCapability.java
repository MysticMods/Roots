package epicsquid.roots.capability;

import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerGroveCapability {

  public NBTTagCompound getData();
  public void setData(NBTTagCompound tag);

}
