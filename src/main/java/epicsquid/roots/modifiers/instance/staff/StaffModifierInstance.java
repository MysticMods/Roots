package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstance;
import epicsquid.roots.modifiers.modifier.Modifier;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Objects;

public class StaffModifierInstance extends BaseModifierInstance {
  private boolean enabled;

  public StaffModifierInstance(Modifier modifier, boolean applied, boolean enabled) {
    super(modifier, applied);
    this.enabled = enabled;
  }

  public StaffModifierInstance() {
    super();
    this.enabled = false;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound tag = super.serializeNBT();
    tag.setBoolean("e", enabled);
    return tag;
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    super.deserializeNBT(tag);
    this.enabled = tag.getBoolean("e");
  }

  public static StaffModifierInstance fromNBT(NBTTagCompound tag) {
    StaffModifierInstance result = new StaffModifierInstance();
    result.deserializeNBT(tag);
    return result;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    if (!this.enabled || !this.applied) {
      return costs;
    }

    return modifier.apply(costs);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StaffModifierInstance that = (StaffModifierInstance) o;
    return applied == that.applied &&
        enabled == that.enabled &&
        modifier.equals(that.modifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(modifier, applied, enabled);
  }
}
