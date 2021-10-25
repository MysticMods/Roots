package epicsquid.mysticallib.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.CompoundNBT;

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

  public CompoundNBT writeNBT() {
    CompoundNBT result = new CompoundNBT();
    result.setLong("cooldown", cooldown);
    return result;
  }

  public void readNBT(NBTBase incoming) {
    if (incoming instanceof CompoundNBT) {
      CompoundNBT tag = (CompoundNBT) incoming;
      this.cooldown = tag.getLong("cooldown");
    } else {
      this.cooldown = 0;
    }
  }
}
