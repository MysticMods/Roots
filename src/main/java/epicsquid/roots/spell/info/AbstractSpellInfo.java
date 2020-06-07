package epicsquid.roots.spell.info;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractSpellInfo implements INBTSerializable<NBTTagCompound> {
  protected SpellBase spell;

  public AbstractSpellInfo() {
  }

  public AbstractSpellInfo(SpellBase spell) {
    this.spell = spell;
  }

  public SpellBase getSpell() {
    return spell;
  }

  public static SpellBase getSpellFromTag (NBTTagCompound nbt) {
    String name = nbt.getString("s");
    if (name.contains(":")) {
      return SpellRegistry.getSpell(new ResourceLocation(name));
    } else {
      return SpellRegistry.getSpell(name);
    }
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = new NBTTagCompound();
    result.setString("s", spell.getRegistryName().toString());
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    String name = nbt.getString("s");
    if (name.contains(":")) {
      this.spell = SpellRegistry.getSpell(new ResourceLocation(name));
    } else {
      this.spell = SpellRegistry.getSpell(name);
    }
  }

  public abstract boolean isEmpty ();
}
