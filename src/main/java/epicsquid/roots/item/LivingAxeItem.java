package epicsquid.roots.item;

import epicsquid.roots.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LivingAxeItem extends AxeItem implements ILivingRepair {
  public LivingAxeItem(IItemTier tier, float attackDamageIn, float attackSpeedIn, Item.Properties builder) {
    // TODO: Fix attack speed
    super(tier, 3, 3, builder);
  }

  @Override
  public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
  }

  @Override
  public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
    return toRepair.getItem() == this && ModItems.barks.contains(repair.getItem());
  }
}
