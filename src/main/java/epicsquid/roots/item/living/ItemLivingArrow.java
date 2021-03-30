package epicsquid.roots.item.living;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.roots.entity.projectile.EntityLivingArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingArrow extends ItemArrowBase {
  public ItemLivingArrow(String name) {
    super(name);
  }

  @Override
  public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
    EntityLivingArrow arrow = new EntityLivingArrow(worldIn, shooter);
    arrow.setPotionEffect(stack);
    arrow.setDamage(4.0D);
    return arrow;
  }
}
