package epicsquid.roots.item;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.roots.entity.projectile.LivingArrowEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LivingArrowItem extends ItemArrowBase {
  public LivingArrowItem(String name) {
    super(name);
  }

  @Override
  public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
    LivingArrowEntity arrow = new LivingArrowEntity(worldIn, shooter);
    arrow.setPotionEffect(stack);
    arrow.setDamage(3.0D);
    return arrow;
  }
}
