package epicsquid.roots.info.storage;

import epicsquid.roots.info.SpellDustInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;

public class DustSpellStorage extends AbstractSpellStorage<DustSpellStorage, SpellDustInfo> {
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
  }

  @Override
  public void previousSlot() {
  }

  @Override
  public void nextSlot() {
  }

  @Override
  public void setSpellToSlot(SpellDustInfo spell) {
    this.info = spell;
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
}
