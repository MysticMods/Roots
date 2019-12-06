package epicsquid.roots.item;

import epicsquid.mysticallib.item.BaseArrowItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class LivingArrowItem extends BaseArrowItem {


  public LivingArrowItem(Properties props, double damage) {
    super(props, damage);
  }

  @Override
  @Nonnull
  public ArrowEntity createArrow(@Nonnull World worldIn, @Nonnull ItemStack stack, LivingEntity shooter) {
/*    LivingArrowEntity arrow = new LivingArrowEntity(worldIn, shooter);
    arrow.setPotionEffect(stack);
    arrow.setDamage(3.0D);
    return arrow;*/
    return EntityType.ARROW.create(worldIn);
  }
}
