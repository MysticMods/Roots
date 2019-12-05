package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.roots.entity.projectile.EntityLivingArrow;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingArrow extends ItemArrowBase {
  public ItemLivingArrow(String name) {
    super(name);
  }

  @Override
  public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
    EntityLivingArrow arrow = new EntityLivingArrow(worldIn, shooter);
    arrow.setPotionEffect(stack);
    arrow.setDamage(3.0D);
    return arrow;
  }
}
