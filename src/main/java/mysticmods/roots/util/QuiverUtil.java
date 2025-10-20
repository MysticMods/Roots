package mysticmods.roots.util;

import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.attachment.QuiverRecord;
import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.integration.curios.CuriosIntegration;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuiverUtil {
  @Nullable
  public static QuiverRecord getQuiverRecord(ItemStack stack) {
    return stack.get(ModAttachments.QUIVER_RECORD);
  }

  @Nullable
  public static UUID getUUID(ItemStack stack) {
    return stack.get(ModAttachments.ITEM_UUID);
  }

  public static UUID getOrCreateUUID(ItemStack stack) {
    UUID value = stack.get(ModAttachments.ITEM_UUID);
    if (value == null) {
      value = UUID.randomUUID();
      stack.set(ModAttachments.ITEM_UUID, value);
    }
    return value;
  }

  public static List<ItemStack> findQuivers(LivingEntity entity) {
    List<ItemStack> result = new ArrayList<>();
    if (entity.getMainHandItem().is(RootsTags.Items.QUIVERS)) {
      result.add(entity.getMainHandItem());
    }
    if (entity.getOffhandItem().is(RootsTags.Items.QUIVERS)) {
      result.add(entity.getOffhandItem());
    }
    if (entity instanceof Player player) {
      result.addAll(CuriosIntegration.getTagged(player, RootsTags.Items.QUIVERS));

      for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
        ItemStack stack = player.getInventory().getItem(i);
        if (stack.is(RootsTags.Items.QUIVERS)) {
          result.add(stack);
        }
      }
    }

    return result;
  }

  public static void consumeAmmunition(LivingEntity entity, QuiverRecord record) {
    ItemStack quiver = findQuiver(entity, record.quiverId());
    if (quiver.isEmpty()) {
      return;
    }

    // TODO: Outsource "6" to a constants file
    NonNullList<ItemStack> contents = NonNullList.withSize(6, ItemStack.EMPTY);
    quiver.getOrDefault(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.EMPTY).copyInto(contents);

    int index = record.slotId();
    if (index >= 0 && index < contents.size()) {
      ItemStack stack = contents.get(index);
      if (!stack.isEmpty() && stack.is(ItemTags.ARROWS)) {
        stack.shrink(1);
        if (stack.isEmpty()) {
          contents.set(index, ItemStack.EMPTY);
        }
        quiver.set(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.fromItems(contents));
      }
    }
  }

  public static ItemStack findQuiver(LivingEntity entity, UUID id) {
    for (ItemStack stack : findQuivers(entity)) {
      if (id.equals(getUUID(stack))) {
        return stack;
      }
    }
    return ItemStack.EMPTY;
  }

  public static ItemStack getArrow(ItemStack quiver) {
    if (quiver.isEmpty() || !quiver.is(RootsTags.Items.QUIVERS)) {
      return ItemStack.EMPTY;
    }
    NonNullList<ItemStack> contents = NonNullList.withSize(6, ItemStack.EMPTY);
    quiver.getOrDefault(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.EMPTY).copyInto(contents);
    ItemStack arrow = ItemStack.EMPTY;
    for (int i = 0; i < contents.size(); i++) {
      ItemStack stack = contents.get(i);
      if (stack.is(ItemTags.ARROWS)) {
        arrow = stack.copy();
        // Important: setting the count to 1 is required to ensure that the arrow is "consumed" properly in `ProjectileWeaponItem.useAmmo`.
        arrow.setCount(1);
        arrow.set(ModAttachments.QUIVER_RECORD, new QuiverRecord(getOrCreateUUID(quiver), i));
        break;
      }
    }

    return arrow;
  }

  public static ItemStack getArrow(Player player) {
    for (ItemStack stack : findQuivers(player)) {
      ItemStack arrow = getArrow(stack);
      if (!arrow.isEmpty()) {
        return arrow;
      }
    }

    return ItemStack.EMPTY;
  }

  public static int countArrows(ItemStack stack) {
    if (stack.isEmpty() || !stack.is(RootsTags.Items.QUIVERS)) {
      return 0;
    }
    int count = 0;
    var contents = stack.getOrDefault(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.EMPTY);
    for (int i = 0; i < contents.getSlots(); i++) {
      if (!contents.getStackInSlot(i).isEmpty()) {
        count += contents.getStackInSlot(i).getCount();
      }
    }
    return count;
  }
}
