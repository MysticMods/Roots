package epicsquid.roots.library;

import epicsquid.roots.spell.SpellBase;
import net.minecraft.nbt.NBTTagCompound;

public class SpellDustInfo extends AbstractSpellInfo {
  public SpellDustInfo() {
  }

  public SpellDustInfo(SpellBase spell) {
    super(spell);
  }

  public static SpellDustInfo fromNBT(NBTTagCompound tag) {
    SpellDustInfo instance = new SpellDustInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
