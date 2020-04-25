package epicsquid.roots.info.storage;

import epicsquid.mysticallib.util.ItemUtil;
import epicsquid.roots.info.AbstractSpellInfo;
import epicsquid.roots.spell.SpellBase;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public abstract class AbstractSpellStorage<T extends AbstractSpellStorage<T, V>, V extends AbstractSpellInfo> implements INBTSerializable<NBTTagCompound> {
  protected ItemStack stack;
  protected int selectedSlot;

  public AbstractSpellStorage() {
  }

  protected AbstractSpellStorage(ItemStack stack) {
    this.stack = stack;
  }

  public abstract boolean hasSpellInSlot();

  public abstract boolean isEmpty();

  @Nullable
  public abstract V getSpellInSlot(int slot);

  public int getCooldownLeft() {
    return -1;
  }

  public int getCooldown () {
    return -1;
  }

  public void setCooldown() {
  }

  @Nullable
  public V getSelectedInfo() {
    return getSpellInSlot(selectedSlot);
  }

  @SideOnly(Side.CLIENT)
  public String formatSelectedSpell() {
    V info = getSelectedInfo();
    if (info == null) {
      return "";
    }

    SpellBase spell = info.getSpell();
    if (spell == null) {
      return "";
    }
    return "(" + spell.getTextColor() + TextFormatting.BOLD + I18n.format("roots.spell." + spell.getName() + ".name") + TextFormatting.RESET + ")";
  }

  public abstract void clearSelectedSlot();

  public int getSelectedSlot() {
    return this.selectedSlot;
  }

  public void setSelectedSlot(int slot) {
    this.selectedSlot = slot;
    saveToStack();
  }

  public abstract void previousSlot();

  public abstract void nextSlot();

  public abstract void setSpellToSlot(V spell);

  public int getNextFreeSlot() {
    return -1;
  }

  public boolean hasFreeSlot() {
    return getNextFreeSlot() != -1;
  }

  @Override
  public abstract NBTTagCompound serializeNBT();

  @Override
  public abstract void deserializeNBT(NBTTagCompound tag);

  public void saveToStack() {
    NBTTagCompound tag = ItemUtil.getOrCreateTag(stack);
    tag.setTag("spell_holder", this.serializeNBT());
  }
}
