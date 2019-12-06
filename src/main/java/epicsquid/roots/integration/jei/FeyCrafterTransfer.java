/*package epicsquid.roots.integration.jei;

import epicsquid.roots.gui.container.FeyCrafterContainer;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FeyCrafterTransfer implements IRecipeTransferInfo<FeyCrafterContainer> {
  @Override
  public Class<FeyCrafterContainer> getContainerClass() {
    return FeyCrafterContainer.class;
  }

  @Override
  public ResourceLocation getRecipeCategoryUid() {
    return JEIRootsPlugin.FEY_CRAFTING;
  }

  @Override
  public boolean canHandle(FeyCrafterContainer container) {
    return true;
  }

  @Override
  public List<Slot> getRecipeSlots(FeyCrafterContainer container) {
    List<Slot> slots = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      slots.add(container.getSlot(i));
    }
    return slots;
  }

  @Override
  public List<Slot> getInventorySlots(FeyCrafterContainer container) {
    List<Slot> slots = new ArrayList<>();
    for (int i = 5; i <= 40; i++) {
      slots.add(container.getSlot(i));
    }
    return slots;
  }
}*/
