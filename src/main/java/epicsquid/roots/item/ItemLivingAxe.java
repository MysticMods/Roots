package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemAxeBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingAxe extends ItemAxeBase implements ILivingRepair {

  public ItemLivingAxe(ToolMaterial material, String name) {
    super(material, name, material.getHarvestLevel(), material.getMaxUses(), material.getEnchantability());
  }

  public ItemLivingAxe(ToolMaterial material, String name, int miningLevel, int durability, int enchantablity) {
    super(material, name, miningLevel,durability,enchantablity);
  }



  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
}
