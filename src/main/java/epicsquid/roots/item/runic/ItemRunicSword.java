package epicsquid.roots.item.runic;

import epicsquid.mysticallib.item.ItemSwordBase;
import epicsquid.roots.item.ILivingRepair;
import epicsquid.roots.recipe.ingredient.RootsIngredients;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemRunicSword extends ItemSwordBase implements ILivingRepair {
  public ItemRunicSword(ToolMaterial material, String name) {
    super(material, name, 1992);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && RootsIngredients.RUNED_OBSIDIAN.test(repair);
  }
}
