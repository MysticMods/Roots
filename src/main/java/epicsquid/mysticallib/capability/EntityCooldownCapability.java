package epicsquid.mysticallib.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class EntityCooldownCapability {
  private long cooldown = 0;

  public boolean canHarvest() {
    return cooldown <= System.currentTimeMillis();
  }

  public long getCooldown() {
    return cooldown;
  }

  /**
   * @param cooldown Cooldown in ticks converted to milliseconds
   */
  public void setCooldown(long cooldown) {

    cooldown = (cooldown / 20) * 1000;
    this.cooldown = System.currentTimeMillis() + cooldown;
  }

  public NBTTagCompound writeNBT() {
    NBTTagCompound result = new NBTTagCompound();
    result.setLong("cooldown", cooldown);
    return result;
  }

  public void readNBT(NBTBase incoming) {
    if (incoming instanceof NBTTagCompound) {
      NBTTagCompound tag = (NBTTagCompound) incoming;
      this.cooldown = tag.getLong("cooldown");
    } else {
      this.cooldown = 0;
    }
  }
}
