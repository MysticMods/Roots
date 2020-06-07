package epicsquid.roots.modifiers.instance.staff;

import epicsquid.roots.modifiers.instance.base.BaseModifierInstanceList;
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
}
