package epicsquid.roots.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotFastCrafting extends Slot {
  private final EntityPlayer player;
  private final ContainerGroveCrafter.InventoryWrapper wrapper;
  /**
   * The number of items that have been crafted so far. Gets passed to ItemStack.onCrafting before being reset.
   */
  public int amountCrafted;


  public SlotFastCrafting(EntityPlayer player, InventoryCraftResult inventory, ContainerGroveCrafter.InventoryWrapper wrapper, int slotIndex, int xPosition, int yPosition) {
    super(inventory, slotIndex, xPosition, yPosition);
    this.player = player;
    this.wrapper = wrapper;
  }

  /**
   * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
   */
  public boolean isItemValid(ItemStack stack) {
    return false;
  }

  /**
   * Decrease the size of the stack in slot (first int arg) by the amount of the second int arg. Returns the new
   * stack.
   */
  public ItemStack decrStackSize(int amount) {
    if (this.getHasStack()) {
      this.amountCrafted += Math.min(amount, this.getStack().getCount());
    }

    return super.decrStackSize(amount);
  }

  /**
   * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood. Typically increases an
   * internal count then calls onCrafting(item).
   */
  protected void onCrafting(ItemStack stack, int amount) {
    this.amountCrafted += amount;
    this.onCrafting(stack);
  }

  protected void onSwapCraft(int p_190900_1_) {
    this.amountCrafted += p_190900_1_;
  }

  /**
   * the itemStack passed in is the output - ie, iron ingots, and pickaxes, not ore and wood.
   */
  protected void onCrafting(ItemStack stack) {
    if (this.amountCrafted > 0) {
      stack.onCrafting(this.player.world, this.player, this.amountCrafted);
    }

    this.amountCrafted = 0;
  }

  @Override
  public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
    this.onCrafting(stack);
    net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);

    for (int i = 0; i < 5; i++) {
      ItemStack inSlot = wrapper.getStackInSlot(i);
      if (!inSlot.isEmpty()) {
        wrapper.decrStackSize(i, 1);
      }
    }

    return stack;
  }
}
