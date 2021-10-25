package epicsquid.roots.item.living;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.roots.entity.projectile.EntityLivingArrow;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingArrow extends ItemArrowBase {
  public ItemLivingArrow(String name) {
    super(name);
    DispenserBlock.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new ProjectileDispenseBehavior() {
      @Override
      protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
        EntityLivingArrow entitytippedarrow = new EntityLivingArrow(worldIn, position.getX(), position.getY(), position.getZ());
        entitytippedarrow.pickupStatus = PickupStatus.ALLOWED;
        return entitytippedarrow;
      }
    });
  }

  @Override
  public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
    EntityLivingArrow arrow = new EntityLivingArrow(worldIn, shooter);
    arrow.setPotionEffect(stack);
    arrow.setDamage(4.0D);
    return arrow;
  }
}
