package mysticmods.roots.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

import java.util.function.Predicate;

/**
 * Utility functions for interacting with items.
 */
@SuppressWarnings("unused")
public class ItemUtil {
  public static Predicate<DataComponentType<?>> FORGETTER = o -> o.equals(DataComponents.DAMAGE) || o.equals(DataComponents.MAX_DAMAGE) || o.equals(DataComponents.UNBREAKABLE);

  /**
   * Compares two itemstacks without considering their size.
   *
   * @param item1 First item to compare.
   * @param item2 Second item to compare.
   * @return True if the items, tags, and capabilities between `item1` and `item2` match, disregarding size.
   */
  public static boolean equalWithoutSize(ItemStack item1, ItemStack item2) {
    return ItemStack.isSameItem(item1, item2);
  }

  public static NonNullList<ItemStack> copyItemList(NonNullList<ItemStack> reference) {
    NonNullList<ItemStack> contents = NonNullList.withSize(reference.size(), ItemStack.EMPTY);
    for (int i = 0; i < reference.size(); i++) {
      contents.set(i, reference.get(i).copy());
    }
    return contents;
  }

  public static ItemStack insertPlayerInventoryStacked(Player player, ItemStack stack, boolean simulate) {
    PlayerMainInvWrapper inv = new PlayerMainInvWrapper(player.getInventory());
    return ItemHandlerHelper.insertItemStacked(inv, stack, simulate);
  }

  public static boolean equalWithoutDamage(ItemStack stack1, ItemStack stack2) {
    if (ItemStack.isSameItemSameComponents(stack1, stack2)) {
      return true;
    }
    if (!stack1.getComponents().has(DataComponents.DAMAGE) || !stack2.getComponents().has(DataComponents.DAMAGE)) {
      return false;
    }
    DataComponentPatch map1 = stack1.getComponentsPatch().forget(FORGETTER);
    DataComponentPatch map2 = stack2.getComponentsPatch().forget(FORGETTER);
    return map1.equals(map2);
  }

  public static class Spawn {
    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack) {
      return spawnItem(world, pos, stack, -1);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, boolean offset) {
      return spawnItem(world, pos, stack, offset, -1);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, int ticks) {
      return spawnItem(world, pos, stack, true, ticks);
    }

    public static ItemEntity spawnItem(Level world, BlockPos pos, ItemStack stack, boolean offset, int ticks) {
      return spawnItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, offset, stack, ticks);
    }

    public static ItemEntity spawnItem(Level world, double x, double y, double z, boolean offset, ItemStack stack, int ticks) {
      if (offset) {
        x += (world.random.nextDouble() - 0.5);
        y += (world.random.nextDouble() - 0.5);
        z += (world.random.nextDouble() - 0.5);
      }
      ItemEntity item = new ItemEntity(world, x, y, z, stack);
      if (ticks != -1) {
        item.setPickUpDelay(ticks);
      }
      return spawnItem(world, item);
    }

    public static ItemEntity spawnItem(Level world, ItemEntity item) {
      item.setDeltaMovement(0, 0, 0);
      world.addFreshEntity(item);
      return item;
    }
  }
}
