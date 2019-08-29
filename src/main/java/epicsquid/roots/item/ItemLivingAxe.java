package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemAxeBase;
import epicsquid.roots.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingAxe extends ItemAxeBase implements ILivingRepair {
  public ItemLivingAxe(ToolMaterial material, String name) {
    super(material, name, 3, 192, 22);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && ModItems.barks.contains(repair.getItem());
  }
}
