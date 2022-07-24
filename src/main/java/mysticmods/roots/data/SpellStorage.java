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
  // TODO: :-?
  private ItemStack stack;

  protected SpellStorage() {
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
