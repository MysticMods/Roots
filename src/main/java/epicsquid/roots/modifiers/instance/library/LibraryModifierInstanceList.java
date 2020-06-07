package epicsquid.roots.modifiers.instance.library;

import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.AbstractSpellInfo;
import net.minecraft.nbt.NBTTagCompound;

public class LibraryModifierInstanceList extends BaseModifierInstanceList<LibraryModifierInstance> {
  public LibraryModifierInstanceList(SpellBase spell) {
    super(spell, LibraryModifierInstance::new);
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    super.deserializeNBT(tag, LibraryModifierInstance::fromNBT);
  }

  public static LibraryModifierInstanceList fromNBT(NBTTagCompound tag) {
    LibraryModifierInstanceList result = new LibraryModifierInstanceList(AbstractSpellInfo.getSpellFromTag(tag));
    result.deserializeNBT(tag);
    return result;
  }
}
