package epicsquid.roots.spell.info.storage;

import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.info.SpellDustInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DustSpellStorage extends AbstractSpellStorage<SpellDustInfo> {
  private SpellDustInfo info = null;

  public DustSpellStorage() {
  }

  protected DustSpellStorage(ItemStack stack) {
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

  @Nullable
  @Override
  public SpellDustInfo getSpellInSlot(int slot) {
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
    SpellDustInfo info = new SpellDustInfo(spell);
    setSpellToSlot(info);
  }

  @Override
  public void setSpellToSlot(SpellDustInfo spell) {
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
    this.info = SpellDustInfo.fromNBT(tag);
  }

  @Nonnull
  public static DustSpellStorage fromStack(ItemStack stack) {
    return fromStack(stack, DustSpellStorage::new);
  }
}
