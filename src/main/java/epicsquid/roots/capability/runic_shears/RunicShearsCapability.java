package epicsquid.roots.capability.runic_shears;

public class RunicShearsCapability {
  private long cooldown = 0;

  public boolean canHarvest() {
    return cooldown <= System.currentTimeMillis();
  }

  public long getCooldown() {
    return cooldown;
  }

  /**
   * @param cooldown Cooldown in ticks/milliseconds
   */
  public void setCooldown(long cooldown) {

    this.cooldown = System.currentTimeMillis() + cooldown;
  }
}
