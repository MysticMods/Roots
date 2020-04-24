package epicsquid.roots.library;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;

// TODO: Not actually a capability
public class StaffSpellStorage implements INBTSerializable<NBTTagCompound> {
  private static int MAX_SPELL_SLOT = 4;
  private static int MIN_SPELL_SLOT = 0;

  private Int2ObjectOpenHashMap<StaffSpellInfo> spells = new Int2ObjectOpenHashMap<>();
  private int selectedSlot = 0;
  private ItemStack stack;

  public StaffSpellStorage(ItemStack stack) {
    this.stack = stack;
  }

  public boolean isDirty() {
    return false;
  }

  // TODO: What is this even used for?
  public boolean hasSpell() {
    if (!spells.isEmpty()) {
      for (Int2ObjectMap.Entry<StaffSpellInfo> entry : this.spells.int2ObjectEntrySet()) {
        if (entry.getValue() != null) {
          return true;
        }
      }
    }

    return false;
  }

  public boolean hasSpellInSlot() {
    return spells.get(this.selectedSlot) != null;
  }

  public boolean isEmpty() {
    for (Int2ObjectMap.Entry<StaffSpellInfo> entry : this.spells.int2ObjectEntrySet()) {
      if (entry.getValue() != null) {
        return false;
      }
    }
    return true;
  }

  @Nullable
  public StaffSpellInfo getSpellInSlot(int slot) {
    if (slot < MIN_SPELL_SLOT || slot > MAX_SPELL_SLOT) {
      throw new IllegalStateException("Tried to get spell for invalid slot " + slot);
    }
    return spells.get(slot);
  }



  public int getCooldown() {
    StaffSpellInfo info = getSelectedSpell();
    if (info == null) {
      return -1;
    }
    return info.cooldown();
  }

  // TODO: Is this used? Is it used usefully?
  @Deprecated
  public void setCooldown() {
    StaffSpellInfo info = getSelectedSpell();
    if (info != null) {
      info.use();
    }
    saveToStack();
  }

  @Deprecated
  public int getLastCooldown() {
    return -1;
  }

  @Deprecated
  public void setLastCooldown(int lastCooldown) {
    saveToStack();
  }

  @Nullable
  public StaffSpellInfo getSelectedSpell() {
    return spells.get(this.selectedSlot);
  }

  @SideOnly(Side.CLIENT)
  public String formatSelectedSpell() {
    StaffSpellInfo info = spells.get(this.selectedSlot);
    if (info == null) {
      return "";
    }

    SpellBase spell = info.getSpell();
    if (spell == null) {
      return "";
    }
    return "(" + spell.getTextColor() + TextFormatting.BOLD + I18n.format("roots.spell." + spell.getName() + ".name") + TextFormatting.RESET + ")";
  }

  public void clearSelectedSlot() {
    spells.put(this.selectedSlot, null);
    saveToStack();
  }

  public int getSelectedSlot() {
    return this.selectedSlot;
  }

  public void setSelectedSlot(int slot) {
    this.selectedSlot = slot;
    saveToStack();
  }

  public void previousSlot() {
    if (this.isEmpty()) {
      setSelectedSlot(0);
      return;
    }

    int originalSlot = selectedSlot;

    for (int i = selectedSlot - 1; i >= 0; i--) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        return;
      }
    }
    for (int i = 5; i >= originalSlot; i--) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        return;
      }
    }

    setSelectedSlot(originalSlot);
  }


  public void nextSlot() {
    if (this.isEmpty()) {
      setSelectedSlot(0);
      return;
    }

    int originalSlot = selectedSlot;

    for (int i = selectedSlot + 1; i < 5; i++) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        return;
      }
    }
    for (int i = 0; i < originalSlot; i++) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        return;
      }
    }

    setSelectedSlot(originalSlot);
  }

  public void setSpellToSlot(SpellBase spell) {
    assert hasFreeSlot();
    setSelectedSlot(getNextFreeSlot());
    this.spells.put(this.selectedSlot, spell);
    /*    this.spellModules.remove(this.selectedSlot);*/
    saveToStack();
  }

/*  public void addModule(SpellModule module) {
    if (this.spellModules.getOrDefault(this.selectedSlot, null) == null) {
      List<SpellModule> modules = new ArrayList<>();
      modules.add(module);
      this.spellModules.put(this.selectedSlot, modules);
    } else {
      List<SpellModule> modules = this.spellModules.get(this.selectedSlot);
      modules.add(module);
    }
    saveToStack();
  }

  public List<SpellModule> getSelectedModules() {
    return this.spellModules.getOrDefault(this.selectedSlot, new ArrayList<>());
  }*/

  public int getNextFreeSlot() {
    for (int i = 0; i < 5; i++) {
      if (spells.getOrDefault(i, null) == null) {
        return i;
      }
    }
    return -1;
  }

  public boolean hasFreeSlot() {
    return getNextFreeSlot() != -1;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound compound = new NBTTagCompound();
    for (Map.Entry<Integer, SpellBase> entry : this.spells.entrySet()) {
      compound.setString("spell_" + entry.getKey(), (entry.getValue() == null) ? "" : entry.getValue().getName());
    }

/*    for (Map.Entry<Integer, List<SpellModule>> entry : this.spellModules.entrySet()) {
      List<SpellModule> modules = entry.getValue();
      for (int i = 0; i < modules.size(); i++) {
        compound.setString(entry.getKey() + "_spell_" + i, modules.get(i).getName());
      }
    }*/

    compound.setInteger("selectedSlot", this.selectedSlot);
    compound.setInteger("cooldown", this.cooldown);
    compound.setInteger("lastCooldown", this.lastCooldown);
    return compound;
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    for (int i = 0; i < 5; i++) {
      if (!tag.getString("spell_" + i).isEmpty()) {
        spells.put(i, SpellRegistry.getSpell(tag.getString("spell_" + i)));
      }
    }

/*    for (int i = 0; i < 5; i++) {
      List<SpellModule> modules = new ArrayList<>();
      for (int j = 0; j < 5; j++) {
        if (!tag.getString(i + "_spell_" + j).isEmpty()) {
          modules.add(ModuleRegistry.getModule(tag.getString(i + "_spell_" + j)));
        }
      }
      if (modules.size() > 0) {
        this.spellModules.put(i, modules);
      }
    }*/

    this.selectedSlot = tag.getInteger("selectedSlot");
    this.cooldown = tag.getInteger("cooldown");
    this.lastCooldown = tag.getInteger("lastCooldown");
  }

  @Nonnull
  public static StaffSpellStorage fromStack(ItemStack stack) {
    StaffSpellStorage result = new StaffSpellStorage(stack);
    NBTTagCompound tag = stack.getTagCompound();
    if (tag != null && tag.hasKey("spell_holder")) {
      result.deserializeNBT(tag.getCompoundTag("spell_holder"));
    }
    return result;
  }

  public void saveToStack() {
    boolean correct = stack.getItem() == ModItems.spell_dust || stack.getItem() == ModItems.staff;
    assert correct;

    NBTTagCompound tag = stack.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      stack.setTagCompound(tag);
    }
    tag.setTag("spell_holder", this.serializeNBT());
  }
}
