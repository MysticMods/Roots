package mysticmods.roots.data;

import mysticmods.roots.RootsTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class SpellStorage {
  private final List<SpellInstance> spells = new ArrayList<>();
  private int index;
  boolean dirty = false;

  protected SpellStorage() {
  }

  // cycle next spell
  // slots?
  // cycle previous spell
  // add spell
  // remove spell
  // adjust modifiers
  // save


  public boolean isDirty() {
    return dirty;
  }

  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }

  public void save (ItemStack toSave) {
    CompoundTag tag = toSave.getOrCreateTag();
    tag.putInt("index", this.index);
    ListTag spells = new ListTag();
    for (SpellInstance spell : this.spells) {
      spells.add(spell.toNBT());
    }
    tag.put("spells", spells);
  }

  @Nullable
  public static SpellStorage fromItem (ItemStack stack) {
    if (!stack.is(RootsTags.Items.CASTING_TOOLS)) {
      return null;
    }

    CompoundTag tag = stack.getTag();
    if (tag == null) {
      return null;
    }

    SpellStorage result = new SpellStorage();

    ListTag spells = tag.getList("spells", Tag.TAG_COMPOUND);
    for (int i = 0; i < spells.size(); i++) {
      result.spells.add(SpellInstance.fromNBT(spells.getCompound(i)));
    }

    result.index = tag.getInt("index");
    return result;
  }
}
