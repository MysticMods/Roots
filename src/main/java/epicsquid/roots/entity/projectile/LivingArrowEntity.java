package epicsquid.roots.entity.projectile;

import epicsquid.roots.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LivingArrowEntity extends ArrowEntity {
  public LivingArrowEntity(World worldIn) {
    super(worldIn);
  }

  public LivingArrowEntity(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public LivingArrowEntity(World worldIn, LivingEntity shooter) {
    super(worldIn, shooter);
  }

  @Override
  protected ItemStack getArrowStack() {
    return new ItemStack(ModItems.living_arrow);
  }
}
