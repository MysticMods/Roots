package epicsquid.roots.capability.playerdata;

import net.minecraft.nbt.CompoundNBT;

public interface IPlayerDataCapability {
  public CompoundNBT getData();

  public void setData(CompoundNBT tag);

  public void markDirty();

  public boolean isDirty();

  public void clean();
}
