package epicsquid.roots.entity.projectile;

import epicsquid.roots.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityLivingArrow extends ArrowEntity {
  public EntityLivingArrow(World worldIn) {
    super(worldIn);
  }

  public EntityLivingArrow(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public EntityLivingArrow(World worldIn, LivingEntity shooter) {
    super(worldIn, shooter);
  }

  @Override
  protected ItemStack getArrowStack() {
    return new ItemStack(ModItems.living_arrow);
  }
}
