package epicsquid.roots.spell.info;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public abstract class AbstractSpellInfo implements INBTSerializable<NBTTagCompound> {
  protected SpellBase spell;

  public AbstractSpellInfo() {
  }

  public AbstractSpellInfo(SpellBase spell) {
    this.spell = spell;
  }

  @Nullable
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
    String name = nbt.getString("s");
    if (name.contains(":")) {
      this.spell = SpellRegistry.getSpell(new ResourceLocation(name));
    } else {
      this.spell = SpellRegistry.getSpell(name);
    }
  }

  public abstract boolean isEmpty ();
}
