package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstance;
import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import net.minecraft.nbt.NBTTagCompound;

public class StaffModifierInstanceList extends BaseModifierInstanceList<StaffModifierInstance> {
  public StaffModifierInstanceList(SpellBase spell) {
    super(spell, StaffModifierInstance::new);
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    super.deserializeNBT(tag, StaffModifierInstance::fromNBT);
  }

  public static StaffModifierInstanceList fromNBT(NBTTagCompound tag) {
    StaffModifierInstanceList result = new StaffModifierInstanceList(AbstractSpellInfo.getSpellFromTag(tag));
    result.deserializeNBT(tag);
    return result;
  }

  public static StaffModifierInstanceList fromLibrary (LibraryModifierInstanceList incoming) {
    StaffModifierInstanceList result = new StaffModifierInstanceList(incoming.getSpell());
    for (LibraryModifierInstance modifier : incoming) {
      result.add(modifier.toStaff());
    }
    return result;
  }

  public LibraryModifierInstanceList toLibrary () {
    LibraryModifierInstanceList result = new LibraryModifierInstanceList(spell);
    for (StaffModifierInstance modifier : this) {
      result.add(modifier);
    }
    return result;
  }
}
