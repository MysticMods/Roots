package epicsquid.roots.recipe;

import epicsquid.roots.Roots;
import epicsquid.roots.capability.pouch.PouchItemHandler;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.ArrayList;
import java.util.List;

public class ApothecaryPouchRecipe extends PyreCraftingRecipe {
  public ApothecaryPouchRecipe(ItemStack result, int xp) {
    super(result, xp);
  }

  public ApothecaryPouchRecipe(ItemStack result) {
    super(result);
  }

  @Override
  public void postCraft(ItemStack output, IItemHandlerModifiable handler) {
    ItemStack oldPouch = null;
    for (int i = 0; i < 5; i++) {
      ItemStack slot = handler.getStackInSlot(i);
      if (slot.getItem() == ModItems.component_pouch) {
        oldPouch = slot;
        break;
      }
    }

    if (oldPouch == null) {
      Roots.logger.error("Couldn't find original Component Pouch!");
    } else {
      // Copy nbt!
      output.setTagCompound(oldPouch.getTagCompound());
      PouchItemHandler handler1 = (PouchItemHandler) oldPouch.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
      PouchItemHandler handler2 = (PouchItemHandler) output.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

      if (handler1 == null || handler2 == null) {
        Roots.logger.error("Pouch handlers are empty!?");
      } else {
        List<ItemStack> herbs = new ArrayList<>();
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < handler1.getInventorySlots(); i++) {
          items.add(handler1.extractItem(i, handler1.getStackInSlot(i).getCount(), false));
        }
        for (int i = handler1.getInventorySlots(); i < handler1.getInventorySlots() + handler1.getHerbSlots(); i++) {
          herbs.add(handler1.extractItem(i, handler1.getStackInSlot(i).getCount(), false));
        }
        for (int i = 0; i < items.size(); i++) {
          handler2.setStackInSlot(i, items.get(i));
        }
        for (int i = handler2.getInventorySlots(); i < handler2.getInventorySlots() + herbs.size(); i++) {
          int q = i - handler2.getInventorySlots();
          handler2.setStackInSlot(i, herbs.get(q));
        }
      }
    }
  }
}
