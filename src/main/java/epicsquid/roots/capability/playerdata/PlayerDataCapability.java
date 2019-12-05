package epicsquid.roots.capability.playerdata;

import net.minecraft.nbt.CompoundNBT;

public class PlayerDataCapability implements IPlayerDataCapability {
  public CompoundNBT tag = new CompoundNBT();
  public boolean dirty = true;

  @Override
  public CompoundNBT getData() {
    return tag;
  }

  @Override
  public void setData(CompoundNBT tag) {
    this.tag = tag;
    markDirty();
  }

  @Override
  public void markDirty() {
    this.dirty = true;
  }

  @Override
  public boolean isDirty() {
    return dirty;
  }

  @Override
  public void clean() {
    this.dirty = false;
  }
}
