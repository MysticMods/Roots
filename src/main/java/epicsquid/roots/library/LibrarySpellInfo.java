package epicsquid.roots.library;

import epicsquid.roots.modifiers.modifier.ModifierList;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class LibrarySpellInfo extends AbstractSpellInfo<ModifierList> {
  private LibrarySpellInfo() {
  }

  private LibrarySpellInfo(SpellBase spell) {
    super(spell);
    this.modifiers = new ModifierList(spell);
  }

  @Nullable
  @Override
  public ModifierList getModifiers() {
    return modifiers;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = super.serializeNBT();
    result.setTag("m", modifiers.serializeNBT());
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    super.deserializeNBT(nbt);
    this.modifiers = new ModifierList(getSpell());
    this.modifiers.deserializeNBT(nbt.getCompoundTag("m"));
  }

  public static LibrarySpellInfo fromNBT(NBTTagCompound tag) {
    LibrarySpellInfo instance = new LibrarySpellInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
