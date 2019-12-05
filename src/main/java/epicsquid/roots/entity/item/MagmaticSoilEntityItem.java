package epicsquid.roots.entity.item;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class MagmaticSoilEntityItem extends ItemEntity {
  public MagmaticSoilEntityItem(World worldIn, double x, double y, double z) {
    super(worldIn, x, y, z);
  }

  public MagmaticSoilEntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
    super(worldIn, x, y, z, stack);
  }

  public MagmaticSoilEntityItem(World worldIn) {
    super(worldIn);
  }

  @Override
  public boolean attackEntityFrom(DamageSource source, float amount) {
    if (this.world.isRemote || this.isDead) {
      return false;
    }

    if (source.isFireDamage()) {
      return false;
    }

    return super.attackEntityFrom(source, amount);
  }

  @Override
  public boolean isEntityInvulnerable(DamageSource source) {
    return source.isFireDamage() || super.isEntityInvulnerable(source);
  }
}
