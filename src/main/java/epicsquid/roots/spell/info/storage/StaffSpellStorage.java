package epicsquid.roots.spell.info.storage;

import epicsquid.roots.Roots;
import epicsquid.roots.spell.info.StaffSpellInfo;
import epicsquid.roots.util.SpellUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

// TODO:
public class StaffSpellStorage extends AbstractSpellStorage<StaffSpellInfo> {
  public static int MAX_SPELL_SLOT = 5;
  public static int MIN_SPELL_SLOT = 1;

  private Int2ObjectOpenHashMap<StaffSpellInfo> spells = new Int2ObjectOpenHashMap<>();

  public StaffSpellStorage(ItemStack stack) {
    this.stack = stack;
  }

  @Override
  public boolean hasSpellInSlot() {
    return spells.get(this.selectedSlot) != null;
  }

  @Override
  public boolean isEmpty() {
    for (StaffSpellInfo v : this.spells.values()) {
      if (v != null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isValid() {
    return SpellUtil.isValidStaff(stack);
  }

  @Override
  @Nullable
  public StaffSpellInfo getSpellInSlot(int slot) {
    if (slot < MIN_SPELL_SLOT || slot > MAX_SPELL_SLOT) {
      throw new IllegalStateException("Tried to get spell for invalid slot " + slot);
    }
    return spells.get(slot);
  }

  public Collection<StaffSpellInfo> getSpells() {
    List<StaffSpellInfo> result = new ArrayList<>();
    for (int i = MIN_SPELL_SLOT; i <= MAX_SPELL_SLOT; i++) {
      StaffSpellInfo info = spells.get(i);
      if (info != null) {
        result.add(info);
      }
    }
    return result;
  }

  public void tick(long cd) {
    boolean ticked = false;
    for (StaffSpellInfo spell : getSpells()) {
      if (spell.tick()) {
        ticked = true;
      }
      if (spell.validate(cd)) {
        ticked = true;
      }
    }
    if (ticked) {
      saveToStack();
    }
  }

  @Override
  public int getCooldownLeft() {
    StaffSpellInfo info = getSelectedInfo();
    if (info == null) {
      return -1;
    }
    return info.cooldownLeft();
  }

  public boolean onCooldown() {
    StaffSpellInfo info = getSelectedInfo();
    if (info == null) {
      return false;
    }
    return info.onCooldown();
  }

  @Override
  public int getCooldown() {
    StaffSpellInfo info = getSelectedInfo();
    if (info == null) {
      return -1;
    }
    return info.cooldownTotal();
  }

  public void setCooldown(long cd) {
    StaffSpellInfo info = getSelectedInfo();
    if (info != null) {
      info.use(cd);
    }
    saveToStack();
  }

  @Override
  @Nullable
  public StaffSpellInfo getSelectedInfo() {
    return spells.get(this.selectedSlot);
  }

  @Override
  public void clearSelectedSlot() {
    spells.put(this.selectedSlot, null);
    saveToStack();
  }

  @Override
  public void clearSlot(int slot) {
    if (slot <= MAX_SPELL_SLOT && slot >= MIN_SPELL_SLOT) {
      spells.put(slot, null);
      saveToStack();
    } else {
      throw new IllegalArgumentException("Invalid argument for StaffSpellStorage::clearSlot, `slot`: " + slot + " cannot be contained within the constraints of " + MIN_SPELL_SLOT + "-" + MAX_SPELL_SLOT);
    }
  }

  @Override
  public void previousSlot() {
    if (this.isEmpty()) {
      // Problematic TODO
      setSelectedSlot(1);
      saveToStack();
      return;
    }

    int originalSlot = selectedSlot;

    for (int i = selectedSlot - 1; i >= MIN_SPELL_SLOT; i--) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        saveToStack();
        return;
      }
    }
    for (int i = MAX_SPELL_SLOT; i >= originalSlot; i--) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        saveToStack();
        return;
      }
    }

    setSelectedSlot(originalSlot);
    saveToStack();
  }

  @Override
  public void nextSlot() {
    if (this.isEmpty()) {
      // Problematic TODO
      setSelectedSlot(1);
      saveToStack();
      return;
    }

    int originalSlot = selectedSlot;

    if (selectedSlot + 1 <= MAX_SPELL_SLOT) {
      for (int i = selectedSlot + 1; i <= MAX_SPELL_SLOT; i++) {
        if (spells.get(i) != null) {
          setSelectedSlot(i);
          saveToStack();
          return;
        }
      }
    }

    for (int i = MIN_SPELL_SLOT; i < originalSlot; i++) {
      if (spells.get(i) != null) {
        setSelectedSlot(i);
        saveToStack();
        return;
      }
    }

    setSelectedSlot(originalSlot);
    saveToStack();
  }

  @Override
  public void addSpell(StaffSpellInfo spell) {
    if (hasFreeSlot()) {
      setSpellToSlot(getNextFreeSlot(), spell);
      saveToStack();
    }
  }

  @Override
  public void setSpellToSlot(int slot, StaffSpellInfo spell) {
    if (slot <= MAX_SPELL_SLOT && slot >= MIN_SPELL_SLOT) {
      this.spells.put(slot, spell);
      saveToStack();
    } else {
      throw new IllegalArgumentException("Invalid argument for StaffSpellStorage::setSpellToSlot, `slot`: " + slot + " cannot be contained within the constraints of " + MIN_SPELL_SLOT + "-" + MAX_SPELL_SLOT);
    }
  }

  @Override
  public int getNextFreeSlot() {
    for (int i = MIN_SPELL_SLOT; i <= MAX_SPELL_SLOT; i++) {
      if (spells.getOrDefault(i, null) == null) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public boolean hasFreeSlot() {
    return getNextFreeSlot() != -1;
  }

  @Override
  public NBTTagCompound serializeNBT() {
    NBTTagCompound compound = new NBTTagCompound();
    NBTTagCompound spells = new NBTTagCompound();
    for (Int2ObjectMap.Entry<StaffSpellInfo> entry : this.spells.int2ObjectEntrySet()) {
      spells.setTag(String.valueOf(entry.getIntKey()), entry.getValue() == null ? new NBTTagCompound() : entry.getValue().serializeNBT());
    }
    compound.setTag("spells", spells);
    compound.setInteger("selectedSlot", this.selectedSlot);
    return compound;
  }

  @Override
  public void deserializeNBT(NBTTagCompound tag) {
    if (tag.hasKey("spells", Constants.NBT.TAG_COMPOUND)) {
      NBTTagCompound spells = tag.getCompoundTag("spells");
      Set<String> keys = spells.getKeySet();
      if (keys.size() > MAX_SPELL_SLOT) {
        Roots.logger.error("Invalid spell when deserializing storage: spells list is " + keys.size() + " which is greater than MAX_SPELL_SLOT " + MAX_SPELL_SLOT + ": " + tag.toString());
      }
      for (String key : keys) {
        int value = Integer.parseInt(key);
        StaffSpellInfo info = StaffSpellInfo.fromNBT(spells.getCompoundTag(key));
        if (info == null) {
          //Probably just spell swapping
          //Roots.logger.error("Tried to deserialize spell " + spells.getCompoundTag(key) + " but got null!");
        } else {
          this.spells.put(value, info);
        }
      }
    } else {
      for (int i = 0; i < 5; i++) {
        if (tag.hasKey("spell_" + i)) {
          spells.put(i, StaffSpellInfo.fromRegistry(tag.getString("spell_" + i)));
        }
      }
    }

    this.selectedSlot = tag.getInteger("selectedSlot");
  }

  @Nullable
  public static StaffSpellStorage fromStack(ItemStack stack) {
    return fromStack(stack, StaffSpellStorage::new);
  }

  @Override
  public void saveToStack() {
    if (getSelectedInfo() == null) {
      for (int i = MIN_SPELL_SLOT; i <= MAX_SPELL_SLOT; i++) {
        if (spells.get(i) != null) {
          setSelectedSlot(i);
          break;
        }
      }
    }
    super.saveToStack();
  }
}
