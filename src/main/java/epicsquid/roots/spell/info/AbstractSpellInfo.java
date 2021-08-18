package epicsquid.roots.spell.info;

import epicsquid.roots.spell.FakeSpell;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
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

  @Nonnull
  public SpellBase getNonNullSpell () {
    if (spell == null) {
      return FakeSpell.INSTANCE;
    }
    return spell;
  }

  public static SpellBase getSpellFromTag(NBTTagCompound nbt) {
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
    if (spell != null) {
      result.setString("s", spell.getRegistryName().toString());
    }
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    if (nbt.hasKey("s")) {
      String name = nbt.getString("s");
      if (name.contains(":")) {
        this.spell = SpellRegistry.getSpell(new ResourceLocation(name));
      } else {
        this.spell = SpellRegistry.getSpell(name);
      }
    } else {
      this.spell = null;
    }
  }

  public abstract boolean isEmpty();

  public abstract ItemStack asStack();
}
