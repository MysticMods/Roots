package epicsquid.roots.modifiers.instance.library;

import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstance;
import epicsquid.roots.modifiers.instance.staff.StaffModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import net.minecraft.nbt.CompoundNBT;

public class LibraryModifierInstanceList extends BaseModifierInstanceList<LibraryModifierInstance> {
  public LibraryModifierInstanceList(SpellBase spell) {
    super(spell, LibraryModifierInstance::new);
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    super.deserializeNBT(tag, LibraryModifierInstance::fromNBT);
  }

  public static LibraryModifierInstanceList fromNBT(CompoundNBT tag) {
    LibraryModifierInstanceList result = new LibraryModifierInstanceList(AbstractSpellInfo.getSpellFromTag(tag));
    result.deserializeNBT(tag);
    return result;
  }

  public static LibraryModifierInstanceList fromStaff(StaffModifierInstanceList incoming) {
    LibraryModifierInstanceList result = new LibraryModifierInstanceList(incoming.getSpell());
    for (StaffModifierInstance modifier : incoming) {
      result.add(modifier);
    }
    return result;
  }

  public StaffModifierInstanceList toStaff() {
    StaffModifierInstanceList result = new StaffModifierInstanceList(spell);
    for (LibraryModifierInstance modifier : this) {
      result.add(modifier.toStaff());
    }
    return result;
  }
}
