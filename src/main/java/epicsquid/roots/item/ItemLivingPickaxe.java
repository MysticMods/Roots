package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemPickaxeBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingPickaxe extends ItemPickaxeBase implements ILivingRepair {
  public ItemLivingPickaxe(ToolMaterial material, String name) {
    super(material, name, material.getHarvestLevel(),material.getMaxUses(),material.getEnchantability());
  }

  public ItemLivingPickaxe(ToolMaterial material, String name,  int miningLevel, int durability, int enchantablity) {
    super(material, name, miningLevel,durability,enchantablity);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
}
