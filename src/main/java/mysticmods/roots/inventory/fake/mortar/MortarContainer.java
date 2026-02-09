package mysticmods.roots.inventory.fake.mortar;

import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModContainers;
import mysticmods.roots.recipe.mortar.MortarInventory;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class MortarContainer extends AbstractContainerMenu {
  private final ContainerLevelAccess access;

  public MortarContainer(int containerId, Inventory inventory) {
    this(containerId, inventory, new MortarInventory(), ContainerLevelAccess.NULL);
  }

  public MortarContainer(int containerId, Inventory inventory, MortarInventory mortarInventory, ContainerLevelAccess access) {
    super(ModContainers.MORTAR.get(), containerId);
    this.access = access;

    for (int i = 0; i < mortarInventory.getSlots(); i++) {
      this.addSlot(new SlotItemHandler(mortarInventory, i, 20 * i, 0) {
        @Override
        public boolean isActive() {
          return false;
        }
      });
    }
  }

  @Override
  public ItemStack quickMoveStack(Player player, int index) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return stillValid(this.access, player, ModBlocks.MORTAR.get());
  }
}
