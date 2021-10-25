package epicsquid.roots.integration.jei;

import epicsquid.roots.container.ContainerFeyCrafter;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;

import java.util.ArrayList;
import java.util.List;

public class FeyCrafterTransfer implements IRecipeTransferInfo<ContainerFeyCrafter> {
  @Override
  public Class<ContainerFeyCrafter> getContainerClass() {
    return ContainerFeyCrafter.class;
  }

  @Override
  public String getRecipeCategoryUid() {
    return JEIRootsPlugin.FEY_CRAFTING;
  }

  @Override
  public boolean canHandle(ContainerFeyCrafter container) {
    return true;
  }

  @Override
  public List<Slot> getRecipeSlots(ContainerFeyCrafter container) {
    List<Slot> slots = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      slots.add(container.getSlot(i));
    }
    return slots;
  }

  @Override
  public List<Slot> getInventorySlots(ContainerFeyCrafter container) {
    List<Slot> slots = new ArrayList<>();
    for (int i = 5; i <= 40; i++) {
      slots.add(container.getSlot(i));
    }
    return slots;
  }
}
