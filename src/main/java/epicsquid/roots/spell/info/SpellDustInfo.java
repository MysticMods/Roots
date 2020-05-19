package epicsquid.roots.spell.info;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.storage.StaffSpellStorage;
import net.minecraft.nbt.NBTTagCompound;

public class SpellDustInfo extends AbstractSpellInfo {
  public static SpellDustInfo EMPTY = new SpellDustInfo();

  public SpellDustInfo() {
  }

  public SpellDustInfo(SpellBase spell) {
    super(spell);
  }

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  public StaffSpellInfo toStaff () {
    return new StaffSpellInfo(getSpell());
  }

  public static SpellDustInfo fromNBT(NBTTagCompound tag) {
    SpellDustInfo instance = new SpellDustInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
