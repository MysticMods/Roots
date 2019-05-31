package epicsquid.roots.integration.jei;

import epicsquid.roots.gui.container.ContainerGroveCrafter;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class GroveCrafterTransfer implements IRecipeTransferInfo<ContainerGroveCrafter> {
  @Override
  public Class<ContainerGroveCrafter> getContainerClass() {
    return ContainerGroveCrafter.class;
  }

  @Override
  public String getRecipeCategoryUid() {
    return JEIRootsPlugin.GROVE_CRAFTING;
  }

  @Override
  public boolean canHandle(ContainerGroveCrafter container) {
    return true;
  }

  @Override
  public List<Slot> getRecipeSlots(ContainerGroveCrafter container) {
    List<Slot> slots = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      slots.add(container.getSlot(i));
    }
    return slots;
  }

  @Override
  public List<Slot> getInventorySlots(ContainerGroveCrafter container) {
    List<Slot> slots = new ArrayList<>();
    for (int i = 5; i < 40; i++) {
      slots.add(container.getSlot(i));
    }
    return slots;
  }
}
