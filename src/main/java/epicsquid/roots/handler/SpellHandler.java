package epicsquid.roots.handler;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.spell.modules.ModuleRegistry;
import epicsquid.roots.spell.modules.SpellModule;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpellHandler implements INBTSerializable<CompoundNBT> {

  private Map<Integer, SpellBase> spells = new Int2ObjectOpenHashMap<>();
  private Map<Integer, List<SpellModule>> spellModules = new Int2ObjectOpenHashMap<>();
  private int selectedSlot = 0;
  private int cooldown = 0;
  private int lastCooldown = 0;
  private ItemStack stack;

  public SpellHandler(ItemStack stack) {
    this.stack = stack;
  }

  public boolean isDirty() {
    return false;
  }

  public boolean hasSpell() {
    if (!spells.isEmpty()) {
      for (Map.Entry<Integer, SpellBase> entry : this.spells.entrySet()) {
        if (entry != null) {
          if (entry.getValue() != null) {
            return true;
          }
        }
      }
    }

    return false;
  }

  public boolean hasSpellInSlot() {
    return spells.getOrDefault(this.selectedSlot, null) != null;
  }

  public boolean isEmpty() {
    return spells.values().stream().filter(Objects::isNull).count() == 5;
  }

  public SpellBase getSpellInSlot(int slot) {
    if (slot < 0 || slot >= 5) return null;
    return spells.getOrDefault(slot, null);
  }

  public int getCooldown() {
    return cooldown;
  }

  public void setCooldown(int cooldown) {
    this.cooldown = cooldown;
    saveToStack();
  }

  public int getLastCooldown() {
    return lastCooldown;
  }

  public void setLastCooldown(int lastCooldown) {
    this.lastCooldown = lastCooldown;
    saveToStack();
  }

  @Nullable
  public SpellBase getSelectedSpell() {
    return spells.get(this.selectedSlot);
  }

  @SideOnly(Side.CLIENT)
  public String formatSelectedSpell() {
    SpellBase spell = spells.get(this.selectedSlot);
    if (spell == null) return "";

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
    this.spellModules.remove(this.selectedSlot);
    saveToStack();
  }

  public void addModule(SpellModule module) {
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
  }

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
  public CompoundNBT serializeNBT() {
    CompoundNBT compound = new CompoundNBT();
    for (Map.Entry<Integer, SpellBase> entry : this.spells.entrySet()) {
      compound.setString("spell_" + entry.getKey(), (entry.getValue() == null) ? "" : entry.getValue().getName());
    }

    for (Map.Entry<Integer, List<SpellModule>> entry : this.spellModules.entrySet()) {
      List<SpellModule> modules = entry.getValue();
      for (int i = 0; i < modules.size(); i++) {
        compound.setString(entry.getKey() + "_spell_" + i, modules.get(i).getName());
      }
    }

    compound.setInteger("selectedSlot", this.selectedSlot);
    compound.setInteger("cooldown", this.cooldown);
    compound.setInteger("lastCooldown", this.lastCooldown);
    return compound;
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {
    for (int i = 0; i < 5; i++) {
      if (!tag.getString("spell_" + i).isEmpty()) {
        spells.put(i, SpellRegistry.getSpell(tag.getString("spell_" + i)));
      }
    }

    for (int i = 0; i < 5; i++) {
      List<SpellModule> modules = new ArrayList<>();
      for (int j = 0; j < 5; j++) {
        if (!tag.getString(i + "_spell_" + j).isEmpty()) {
          modules.add(ModuleRegistry.getModule(tag.getString(i + "_spell_" + j)));
        }
      }
      if (modules.size() > 0) {
        this.spellModules.put(i, modules);
      }
    }

    this.selectedSlot = tag.getInteger("selectedSlot");
    this.cooldown = tag.getInteger("cooldown");
    this.lastCooldown = tag.getInteger("lastCooldown");
  }

  @Nonnull
  public static SpellHandler fromStack(ItemStack stack) {
    boolean correct = stack.getItem() == ModItems.spell_dust || stack.getItem() == ModItems.staff;

    SpellHandler result = new SpellHandler(stack);
    CompoundNBT tag = stack.getTagCompound();
    if (tag != null && tag.hasKey("spell_holder")) {
      result.deserializeNBT(tag.getCompoundTag("spell_holder"));
    }
    return result;
  }

  public void saveToStack() {
    boolean correct = stack.getItem() == ModItems.spell_dust || stack.getItem() == ModItems.staff;
    assert correct;

    CompoundNBT tag = stack.getTagCompound();
    if (tag == null) {
      tag = new CompoundNBT();
      stack.setTagCompound(tag);
    }
    tag.setTag("spell_holder", this.serializeNBT());
  }
}
