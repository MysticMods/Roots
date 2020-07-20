package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.CostType;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierList;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class StaffModifierInstance extends LibraryModifierInstance {
  private boolean enabled;

  public StaffModifierInstance(Modifier modifier, boolean applied, boolean enabled) {
    super(modifier, applied);
    this.enabled = enabled;
  }

  public StaffModifierInstance() {
    super();
    this.enabled = false;
  }

  public StaffModifierInstance(Modifier modifier) {
    this(modifier, false, false);
  }

  public boolean isEnabled() {
    return enabled && !isDisabled();
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

  public static StaffModifierInstance fromLibrary(LibraryModifierInstance instance) {
    return new StaffModifierInstance(instance.getModifier(), instance.isApplied(), false);
  }

  public LibraryModifierInstance toLibrary() {
    return this;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    if (!this.enabled || !this.applied) {
      return costs;
    }

    for (CostType type : CostType.values()) {
      costs = apply(costs, type);
    }

    return costs;
  }

  public boolean isConflicting(StaffModifierInstanceList modifiers) {
    return !getConflicts(modifiers).isEmpty();
  }

  public List<StaffModifierInstance> getConflicts(StaffModifierInstanceList modifiers) {
    Set<IModifier> conflicts = getConflicts();
    if (conflicts.isEmpty()) {
      return Collections.emptyList();
    }

    List<StaffModifierInstance> conflicting = new ArrayList<>();
    for (StaffModifierInstance modifierInstance : modifiers) {
      if (modifierInstance.isEnabled() && modifierInstance.isApplied() && conflicts(modifierInstance)) {
        conflicting.add(modifierInstance);
      }
    }

    return conflicting;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs, CostType phase) {
    return modifier.apply(costs, phase);
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
