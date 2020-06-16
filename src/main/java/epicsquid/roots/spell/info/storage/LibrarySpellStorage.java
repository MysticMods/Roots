package epicsquid.roots.spell.info.storage;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.spell.info.SpellDustInfo;
import epicsquid.roots.util.SpellUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LibrarySpellStorage extends AbstractSpellStorage<LibrarySpellInfo> {
  private LibrarySpellInfo info = null;

  public LibrarySpellStorage() {
  }

  protected LibrarySpellStorage(ItemStack stack) {
    super(stack);
  }

  @Override
  public boolean hasSpellInSlot() {
    return info != null;
  }

  @Override
  public boolean isEmpty() {
    return info != null;
  }

  @Override
  public boolean isValid() {
    return SpellUtil.isValidDust(stack);
  }

  @Nullable
  @Override
  public LibrarySpellInfo getSpellInSlot(int slot) {
    return info;
  }

  @Override
  public void clearSelectedSlot() {
    this.info = null;
    saveToStack();
  }

  @Override
  public void previousSlot() {
  }

  @Override
  public void nextSlot() {
  }

  public void setSpellToSlot(SpellBase spell) {
    LibrarySpellInfo info = new LibrarySpellInfo(spell);
    setSpellToSlot(info);
  }

  @Override
  public void setSpellToSlot(LibrarySpellInfo spell) {
    this.info = spell;
    saveToStack();
  }

  @Override
  public NBTTagCompound serializeNBT() {
    if (this.info == null) {
      return new NBTTagCompound();
    } else {
      return this.info.serializeNBT();
    }
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    this.info = LibrarySpellInfo.fromNBT(tag);
  }

  @Nullable
  public static LibrarySpellStorage fromStack(ItemStack stack) {
    return fromStack(stack, LibrarySpellStorage::new);
  }
}
