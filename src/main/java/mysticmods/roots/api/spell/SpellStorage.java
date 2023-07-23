package mysticmods.roots.api.spell;

import com.google.common.collect.ImmutableList;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.item.ICastingItem;
import mysticmods.roots.api.modifier.Modifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.*;

// NOTE: THIS IS 0 INDEXED
public class SpellStorage {
  private final List<SpellInstance> spells;
  private int index = 0;
  private boolean dirty = false;
  private List<Entry> entryList = null;

  protected SpellStorage(int size) {
    spells = Arrays.asList(new SpellInstance[size]);
  }

  // adjust modifiers
  protected void validateSlot(int index) {
    if (index < 0 || index >= spells.size()) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for SpellStorage of size " + spells.size());
    }
  }

  public int size() {
    return spells.size();
  }

  public int getSlot () {
    return index;
  }

  public int getCooldown() {
    SpellInstance spell = getSpell();
    if (spell != null) {
      return spell.getCooldown();
    }

    return 0;
  }

  public int getMaxCooldown () {
    SpellInstance spell = getSpell();
    if (spell != null) {
      return spell.getMaxCooldown();
    }

    return 0;
  }

  public boolean tick () {
    for (SpellInstance spell : spells) {
      if (spell != null) {
        if (spell.tick()) {
          setDirty(true);
        }
      }
    }
    return isDirty();
  }

  public void nextSpell() {
    int lastIndex = index;
    if (index + 1 < spells.size()) {
      for (int i = index + 1; i < spells.size(); i++) {
        if (spells.get(i) != null) {
          index = i;
          setDirty(true);
          return;
        }
      }
    }
    for (int i = 0; i < lastIndex; i++) {
      if (spells.get(i) != null) {
        index = i;
        setDirty(true);
        return;
      }
    }
  }

  public void previousSpell() {
    int lastIndex = index;
    for (int i = index; i >= 0; i--) {
      if (spells.get(i) != null) {
        index = i;
        setDirty(true);
        return;
      }
    }
    for (int i = spells.size() - 1; i > lastIndex; i--) {
      if (spells.get(i) != null) {
        index = i;
        setDirty(true);
        return;
      }
    }
  }

  public void setSlot(int slot) {
    validateSlot(slot);
    this.setDirty(true);
    this.index = slot;
  }

  public int addSpell(Spell spell, Collection<Modifier> modifiers) {
    int slot = -1;
    for (int i = 0; i < spells.size(); i++) {
      if (getSpell(i) == null) {
        slot = i;
        break;
      }
    }
    if (slot == -1) {
      return -1;
    }
    setDirty(true);
    setSpell(slot, spell, modifiers);
    return slot;
  }

  public boolean setSpell(int slot, Spell spell, Collection<Modifier> modifiers) {
    validateSlot(slot);
    this.setDirty(true);
    return this.spells.set(slot, new SpellInstance(spell, modifiers)) == null;
  }

  @Nullable
  public SpellInstance getSpell(int slot) {
    validateSlot(slot);
    SpellInstance inSlot = this.spells.get(slot);
    if (inSlot == null) {
      return null;
    }
    return new StorageInstance(inSlot);
  }

  @Nullable
  public SpellInstance getSpell() {
    SpellInstance inSlot = this.spells.get(index);
    if (inSlot == null) {
      return null;
    }
    return new StorageInstance(inSlot);
  }

  @Nullable
  public SpellInstance remove(int slot) {
    validateSlot(slot);
    SpellInstance result = this.spells.remove(slot);
    if (result != null) {
      this.setDirty(true);
    }
    return result;
  }

  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    if (dirty) {
      this.entryList = null;
    }
    this.dirty = dirty;
  }

  public void save(ItemStack toSave) {
    CompoundTag tag = toSave.getOrCreateTag();
    tag.putInt("index", this.index);
    ListTag spells = new ListTag();
    for (int i = 0; i < this.spells.size(); i++) {
      SpellInstance spell = this.spells.get(i);
      if (spell != null) {
        CompoundTag thisTag = new CompoundTag();
        thisTag.putInt("index", i);
        thisTag.put("spell", spell.toNBT());
        spells.add(thisTag);
      }
    }
    tag.put("spells", spells);
    tag.putInt("count", this.spells.size());
  }

  public static SpellStorage getOrCreate (ItemStack stack) {
    return fromItem(stack, true);
  }

  @Nullable
  public static SpellStorage fromItem(ItemStack stack) {
    return fromItem(stack, false);
  }

  @Nullable
  public static SpellStorage fromItem(ItemStack stack, boolean create) {
    if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
      return null;
    }

    CompoundTag tag = stack.getTag();
    if (tag == null) {
      if (!create || !(stack.getItem() instanceof ICastingItem castingItem)) {
        return null;
      }

      SpellStorage result = new SpellStorage(castingItem.getSlots());
      result.setDirty(true);
      return result;
    }

    int size = tag.getInt("count");

    SpellStorage result = new SpellStorage(size);

    ListTag spells = tag.getList("spells", Tag.TAG_COMPOUND);
    for (int i = 0; i < spells.size(); i++) {
      CompoundTag thisTag = spells.getCompound(i);
      result.spells.set(thisTag.getInt("index"), SpellInstance.fromNBT(thisTag.getCompound("spell")));
    }

    result.index = tag.getInt("index");
    return result;
  }

  public List<Entry> entryList () {
    if (entryList == null) {
      List<Entry> entryList = new ArrayList<>();
      for (int i = 0; i < size(); i++) {
        entryList.add(new Entry(i, getSpell(i)));
      }
      this.entryList = ImmutableList.copyOf(entryList);
    }
    return entryList;
  }

  protected class StorageInstance extends SpellInstance {
    private final SpellInstance instance;

    public StorageInstance(SpellInstance instance) {
      super(instance.getSpell());
      this.instance = instance;
    }

    @Override
    public Spell getSpell() {
      return instance.getSpell();
    }

    @Override
    public Set<Modifier> getEnabledModifiers() {
      return instance.getEnabledModifiers();
    }

    @Override
    public int getCooldown() {
      return instance.getCooldown();
    }

    @Override
    public void setCooldown(int cooldown) {
      SpellStorage.this.setDirty(true);
      instance.setCooldown(cooldown);
    }

    // TODO: add/remove modifiers; this should just pass on to the spell instance

    @Override
    public boolean hasModifier(Modifier modifier) {
      return instance.hasModifier(modifier);
    }

    @Override
    public void addModifier(Modifier modifier) {
      SpellStorage.this.setDirty(true);
      super.addModifier(modifier);
    }

    @Override
    public void removeModifier(Modifier modifier) {
      SpellStorage.this.setDirty(true);
      super.removeModifier(modifier);
    }

    @Override
    public CompoundTag toNBT() {
      return instance.toNBT();
    }

    @Override
    public boolean equals(Object o) {
      return instance.equals(o);
    }

    @Override
    public int hashCode() {
      return instance.hashCode();
    }
  }

  public static class Entry {
    private final int slot;
    private final SpellInstance spell;

    public Entry(int slot, SpellInstance spell) {
      this.slot = slot;
      this.spell = spell;
    }

    public int getSlot() {
      return slot;
    }

    public SpellInstance getSpell() {
      return spell;
    }
  }
}
