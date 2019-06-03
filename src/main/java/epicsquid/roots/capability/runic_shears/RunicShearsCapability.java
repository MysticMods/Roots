package epicsquid.roots.capability.runic_shears;

import net.minecraft.nbt.NBTTagLong;
import net.minecraftforge.common.util.INBTSerializable;

public class RunicShearsCapability implements INBTSerializable<NBTTagLong> {
  private long cooldown = 0;

  public boolean canHarvest() {
    return cooldown == 0;
  }

  public int getCooldown() {
    return 0;
  }

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
  }

  @Override
  public NBTTagLong serializeNBT() {
    return new NBTTagLong(cooldown);
  }

  @Override
  public void deserializeNBT(NBTTagLong nbt) {
    this.cooldown = nbt.getLong();
  }
}
