package mysticmods.roots.inventory.pouch.component;

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

public class ComponentPouchMenu extends PouchMenu {
  public ComponentPouchMenu(ItemStack inventoryItem) {
    super(inventoryItem);
  }

  @Override
  public int getContainerSize() {
    return 18;
  }

  @Override
  public Component getDisplayName() {
    return CommonComponents.EMPTY;
  }

  @Override
  public void readFromStack(ItemStack stack) {
    inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    stack.getOrDefault(ModAttachments.COMPONENT_POUCH_CONTENTS, ItemContainerContents.EMPTY).copyInto(inventory);
  }

  @Override
  public void writeToStack(ItemStack stack) {
    stack.set(ModAttachments.COMPONENT_POUCH_CONTENTS, ItemContainerContents.fromItems(inventory));
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new ComponentPouchContainer(containerId, playerInventory, this);
  }
}
