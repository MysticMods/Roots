package epicsquid.roots.library;

import epicsquid.roots.spell.FakeSpell;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class SpellInstance implements INBTSerializable<NBTTagCompound> {
  protected SpellBase spell = FakeSpell.INSTANCE;

  public SpellBase getSpell() {
    return spell;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = new NBTTagCompound();
    result.setString("s", spell.getRegistryName().toString());
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    this.spell = SpellRegistry.getSpell(nbt.getString("s"));
  }
}
