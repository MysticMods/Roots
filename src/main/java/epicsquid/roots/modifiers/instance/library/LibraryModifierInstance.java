package epicsquid.roots.modifiers.instance.library;

import epicsquid.roots.api.Herb;
import epicsquid.roots.modifiers.instance.base.BaseModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.modifier.Modifier;
import it.unimi.dsi.fastutil.objects.Object2DoubleOpenHashMap;
import net.minecraft.nbt.NBTTagCompound;

public class LibraryModifierInstance extends BaseModifierInstance {
  public LibraryModifierInstance(Modifier modifier, boolean applied) {
    super(modifier, applied);
  }

  public LibraryModifierInstance (Modifier modifier) {
    super(modifier, false);
  }

  public LibraryModifierInstance() {
  }

  public static LibraryModifierInstance fromNBT(NBTTagCompound tag) {
    LibraryModifierInstance result = new LibraryModifierInstance();
    result.deserializeNBT(tag);
    return result;
  }

  @Override
  public Object2DoubleOpenHashMap<Herb> apply(Object2DoubleOpenHashMap<Herb> costs) {
    return costs;
  }
}
