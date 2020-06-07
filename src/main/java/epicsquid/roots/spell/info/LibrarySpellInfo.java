package epicsquid.roots.spell.info;

import epicsquid.roots.modifiers.instance.library.LibraryModifierInstanceList;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class LibrarySpellInfo extends AbstractSpellModifiers<LibraryModifierInstanceList> {
  public static LibrarySpellInfo EMPTY = new LibrarySpellInfo();

  private boolean obtained;

  private LibrarySpellInfo() {
  }

  public LibrarySpellInfo(SpellBase spell) {
    super(spell);
    this.modifiers = new LibraryModifierInstanceList(spell);
    this.obtained = false;
  }

  @Nullable
  @Override
  public LibraryModifierInstanceList getModifiers() {
    return modifiers;
  }

  public boolean isObtained() {
    return obtained;
  }

  public void setObtained() {
    setObtained(true);
  }

  public void setObtained(boolean value) {
    this.obtained = value;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound result = super.serializeNBT();
    result.setTag("m", modifiers.serializeNBT());
    result.setBoolean("o", obtained);
    return result;
  }

  @Override
  public void deserializeNBT(NBTTagCompound nbt) {
    super.deserializeNBT(nbt);
    this.modifiers = new LibraryModifierInstanceList(getSpell());
    this.modifiers.deserializeNBT(nbt.getCompoundTag("m"));
    this.obtained = nbt.getBoolean("o");
  }

  @Override
  public boolean isEmpty() {
    return this == EMPTY;
  }

  public StaffSpellInfo toStaff() {
    return new StaffSpellInfo(getSpell());
  }

  public static LibrarySpellInfo fromNBT(NBTTagCompound tag) {
    LibrarySpellInfo instance = new LibrarySpellInfo();
    instance.deserializeNBT(tag);
    return instance;
  }
}
