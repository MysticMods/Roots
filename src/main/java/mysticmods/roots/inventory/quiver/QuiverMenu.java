package mysticmods.roots.inventory.quiver;

import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.inventory.pouch.PouchMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

public class QuiverMenu extends PouchMenu {
  public QuiverMenu(ItemStack inventoryItem) {
    super(inventoryItem);
  }

  @Override
  public int getContainerSize() {
    return 6;
  }

  @Override
  public Component getDisplayName() {
    return CommonComponents.EMPTY;
  }

  @Override
  public void readFromStack(ItemStack stack) {
    inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    stack.getOrDefault(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.EMPTY).copyInto(inventory);
  }

  @Override
  public void writeToStack(ItemStack stack) {
    stack.set(ModAttachments.QUIVER_CONTENTS, ItemContainerContents.fromItems(inventory));
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new QuiverContainer(containerId, playerInventory, this);
  }
}
