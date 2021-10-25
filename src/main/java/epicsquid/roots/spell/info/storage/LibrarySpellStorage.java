package epicsquid.roots.spell.info.storage;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.LibrarySpellInfo;
import epicsquid.roots.util.SpellUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

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
  public void clearSlot(int slot) {
    clearSelectedSlot();
  }

  @Override
  public void previousSlot() {
  }

  @Override
  public void nextSlot() {
  }

  public void setSpellToSlot(SpellBase spell) {
    LibrarySpellInfo info = new LibrarySpellInfo(spell);
    addSpell(info);
  }

  @Override
  public void addSpell(LibrarySpellInfo spell) {
    this.info = spell;
    saveToStack();
  }

  @Override
  public void setSpellToSlot(int slot, LibrarySpellInfo spell) {
    addSpell(spell);
  }

  @Override
  public CompoundNBT serializeNBT() {
    if (this.info == null) {
      return new CompoundNBT();
    } else {
      return this.info.serializeNBT();
    }
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    this.info = LibrarySpellInfo.fromNBT(tag);
  }

  @Nullable
  public static LibrarySpellStorage fromStack(ItemStack stack) {
    return fromStack(stack, LibrarySpellStorage::new);
  }
}
