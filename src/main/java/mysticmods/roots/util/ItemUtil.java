package mysticmods.roots.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Utility functions for interacting with items.
 */
@SuppressWarnings("unused")
public class ItemUtil {
  /**
   * Compares two itemstacks without considering their size.
   *
   * @param item1 First item to compare.
   * @param item2 Second item to compare.
   * @return True if the items, tags, and capabilities between `item1` and `item2` match, disregarding size.
   */
  public static boolean equalWithoutSize(ItemStack item1, ItemStack item2) {
    if (item1.getItem() != item2.getItem()) {
      return false;
/*    } else if (item1.getTag() == null && item2.getTag() != null) {
      return false;
    } else {
      return (item1.getTag() == null || item1.getTag().equals(item2.getTag())) && item1.areCapsCompatible(item2);*/
    }

    return false;
  }

  public static NonNullList<ItemStack> copyItemList(NonNullList<ItemStack> reference) {
    NonNullList<ItemStack> contents = NonNullList.withSize(reference.size(), ItemStack.EMPTY);
    for (int i = 0; i < reference.size(); i++) {
      contents.set(i, reference.get(i).copy());
    }
    return contents;
  }

  public static boolean equalWithoutDamage(ItemStack stack1, ItemStack stack2) {
    // TODO:
    return false;
    /*    return ItemStack.isSameItemSameTags(stack1, stack2);*/
  }

  public static class Spawn {
    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack) {
      return spawnItem(world, pos, stack, -1);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, float hoverStart) {
      return spawnItem(world, pos, stack, true, -1, hoverStart);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, int ticks) {
      return spawnItem(world, pos, stack, true, ticks, -1);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, boolean offset) {
      return spawnItem(world, pos, stack, offset, -1, -1);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, boolean offset, int ticks, float hoverStart) {
      return spawnItem(world, pos.getX(), pos.getY(), pos.getZ(), offset, stack, ticks, hoverStart);
    }

    public static ItemEntity spawnItem(Level world, double x, double y, double z, boolean offset, ItemStack stack, int ticks, float hoverStart) {
      if (offset) {
        x += 0.5;
        y += 0.5;
        z += 0.5;
      }
      ItemEntity item = new ItemEntity(world, x, y, z, stack);
      if (ticks != -1) {
        item.setPickUpDelay(ticks);
      }
/*      if (hoverStart != -1) {
        item.bobOffs = hoverStart;
      }*/
      return spawnItem(world, item);
    }

    public static ItemEntity spawnItem(Level world, ItemEntity item) {
      item.setDeltaMovement(0, 0, 0);
      world.addFreshEntity(item);
      return item;
    }
  }
}
