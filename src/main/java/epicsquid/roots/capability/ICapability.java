package epicsquid.roots.capability;

import net.minecraft.nbt.CompoundNBT;

public interface ICapability {
  CompoundNBT getData();

  void setData(CompoundNBT tag);

  void markDirty();

  boolean isDirty();

  void clean();
}
