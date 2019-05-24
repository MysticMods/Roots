package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemShovelBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingShovel extends ItemShovelBase implements ILivingRepair {
  public ItemLivingShovel(ToolMaterial material, String name) {
    super(material, name, 3, 192, 22);
  }

  @Override
  public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
    update(stack, worldIn, entityIn, itemSlot, isSelected);
    super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
  }
}
