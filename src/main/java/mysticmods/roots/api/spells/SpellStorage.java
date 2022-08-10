package mysticmods.roots.api.spells;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.modifier.Modifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

// NOTE: THIS IS 0 INDEXED
public class SpellStorage {
  private final List<SpellInstance> spells;
  private int index = 0;
  boolean dirty = false;

  protected SpellStorage(int size) {
    spells = Arrays.asList(new SpellInstance[size]);
  }

  // adjust modifiers
  protected void validateSlot (int index) {
    if (index < 0 || index >= spells.size()) {
      throw new IndexOutOfBoundsException("Index " + index + " is out of bounds for SpellStorage of size " + spells.size());
    }
  }

  public int size () {
    return spells.size();
  }

  public void next () {
    int lastIndex = index;
    for (int i = index; i < spells.size(); i++) {
      if (spells.get(i) != null) {
        index = i;
        setDirty(true);
        return;
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

  public void previous () {
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

  public void set (int slot) {
    validateSlot(slot);
    this.setDirty(true);
    this.index = slot;
  }

  public int add (Spell spell, List<Modifier> modifiers) {
    int slot = -1;
    for (int i = 0; i < spells.size(); i++) {
      if (get(i) == null) {
        slot = i;
        break;
      }
    }
    if (slot == -1) {
      return -1;
    }
    setDirty(true);
    set(slot, spell, modifiers);
    return slot;
  }

  public boolean set (int slot, Spell spell, List<Modifier> modifiers) {
    validateSlot(slot);
    this.setDirty(true);
    return this.spells.set(slot, new SpellInstance(spell, modifiers)) == null;
  }

  @Nullable
  public SpellInstance get (int slot) {
    validateSlot(slot);
    return new StorageInstance(this.spells.get(slot));
  }

  @Nullable
  public SpellInstance get () {
    return new StorageInstance(this.spells.get(this.index));
  }

  @Nullable
  public SpellInstance remove (int slot) {
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
    this.dirty = dirty;
  }

  public void save(ItemStack toSave) {
    CompoundTag tag = toSave.getOrCreateTag();
    tag.putInt("index", this.index);
    ListTag spells = new ListTag();
    for (int i = 0; i < this.spells.size(); i++) {
      CompoundTag thisTag = new CompoundTag();
      thisTag.putInt("index", i);
      thisTag.put("spell", this.spells.get(i).toNBT());
    }
    tag.put("spells", spells);
    tag.putInt("count", this.spells.size());
  }

  @Nullable
  public static SpellStorage fromItem(ItemStack stack) {
    if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
      return null;
    }

    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    // TODO: checking for stuff

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
    public long getCooldown() {
      return instance.getCooldown();
    }

    @Override
    public void setCooldown(long cooldown) {
      SpellStorage.this.setDirty(true);
      instance.setCooldown(cooldown);
    }

    // TODO: add/remove modifiers

    @Override
    public boolean hasModifier(Modifier modifier) {
      return instance.hasModifier(modifier);
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
}
