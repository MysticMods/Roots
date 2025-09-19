package mysticmods.roots.inventory.pouch.herb;

import mysticmods.roots.init.ModAttachments;
import mysticmods.roots.inventory.pouch.PouchMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import org.jetbrains.annotations.Nullable;

public class HerbPouchMenu extends PouchMenu {
  public HerbPouchMenu(ItemStack inventoryItem) {
    super(inventoryItem);
  }

  @Override
  public int getContainerSize() {
    return 9;
  }

  @Override
  public Component getDisplayName() {
    return Component.translatable("roots.container.herb_pouch");
  }

  @Override
  public void readFromStack(ItemStack stack) {
    inventory = NonNullList.withSize(getContainerSize(), ItemStack.EMPTY);
    stack.getOrDefault(ModAttachments.HERB_POUCH_CONTENTS, ItemContainerContents.EMPTY).copyInto(inventory);
  }

  @Override
  public void writeToStack(ItemStack stack) {
    stack.set(ModAttachments.HERB_POUCH_CONTENTS, ItemContainerContents.fromItems(inventory));
  }

  @Override
  public @Nullable AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
    return new HerbPouchContainer(containerId, playerInventory, this);
  }
}

