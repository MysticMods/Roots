package epicsquid.roots.recipe;

import epicsquid.roots.init.ModItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.oredict.DyeUtils;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;

public class DyeRecipe extends IForgeRegistryEntry.Impl<IRecipe> implements IRecipe {
  @Override
  public boolean isDynamic() {
    return true;
  }

  @Override
  public boolean matches(InventoryCrafting inv, World worldIn) {
    boolean foundPouch = false;
    boolean foundDye = false;

    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack inSlot = inv.getStackInSlot(i);
      if (inSlot.isEmpty()) {
        continue;
      }
      if (inSlot.getItem() == ModItems.apothecary_pouch || inSlot.getItem() == ModItems.component_pouch || inSlot.getItem() == ModItems.herb_pouch || inSlot.getItem() == ModItems.fey_pouch) {
        if (foundPouch) {
          return false;
        }

        foundPouch = true;
      } else if (DyeUtils.isDye(inSlot)) {
        if (foundDye) {
          return false;
        }

        foundDye = true;
      }
    }
    return foundPouch && foundDye;
  }

  @Override
  public ItemStack getCraftingResult(InventoryCrafting inv) {
    ItemStack pouch = ItemStack.EMPTY;
    ItemStack dye = ItemStack.EMPTY;

    for (int i = 0; i < inv.getSizeInventory(); i++) {
      ItemStack inSlot = inv.getStackInSlot(i);
      if (inSlot.getItem() == ModItems.component_pouch || inSlot.getItem() == ModItems.apothecary_pouch || inSlot.getItem() == ModItems.herb_pouch || inSlot.getItem() == ModItems.fey_pouch) {
        pouch = inSlot;
      } else if (DyeUtils.isDye(inSlot)) {
        dye = inSlot;
      }
      if (!pouch.isEmpty() && !dye.isEmpty()) {
        break;
      }
    }

    if (pouch.isEmpty() || dye.isEmpty()) {
      return ItemStack.EMPTY;
    }

    int dyeColor = DyeUtils.metaFromStack(dye).orElse(-1);
    if (dyeColor == -1) {
      return ItemStack.EMPTY;
    }

    ItemStack result = pouch.copy();
    NBTTagCompound tag = result.getTagCompound();
    if (tag == null) {
      tag = new NBTTagCompound();
      result.setTagCompound(tag);
    }

    tag.setInteger("color", dyeColor);
    return result;
  }

  @Override
  public boolean canFit(int width, int height) {
    return width > 1 || height > 1;
  }

  @Nonnull
  @Override
  public ItemStack getRecipeOutput() {
    return ItemStack.EMPTY;
  }
}
