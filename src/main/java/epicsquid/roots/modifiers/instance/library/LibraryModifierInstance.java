package epicsquid.roots.modifiers.instance.library;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.CostType;
import epicsquid.roots.modifiers.IModifier;
import epicsquid.roots.modifiers.IModifierCost;
import epicsquid.roots.modifiers.Modifier;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class LibraryModifierInstance extends BaseModifierInstance {
  public LibraryModifierInstance(Modifier modifier, boolean applied) {
    super(modifier, applied);
  }

  public LibraryModifierInstance(Modifier modifier) {
    super(modifier, false);
  }

  public LibraryModifierInstance() {
  }

  public static LibraryModifierInstance fromNBT(NBTTagCompound tag) {
    LibraryModifierInstance result = new LibraryModifierInstance();
    result.deserializeNBT(tag);
    return result;
  }

  public static LibraryModifierInstance fromStaff(StaffModifierInstance instance) {
    return new LibraryModifierInstance(instance.getModifier(), instance.isApplied());
  }

  public StaffModifierInstance toStaff() {
    return new StaffModifierInstance(modifier, applied, false);
  }

  @Override
  public ItemStack getStack() {
    return ItemStack.EMPTY;
  }

  @Override
  public List<IModifierCost> getCosts() {
    return Collections.emptyList();
  }

  @Override
  public Set<IModifier> getConflicts() {
    return modifier.getConflicts();
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs, CostType phase) {
    return costs;
  }
}
