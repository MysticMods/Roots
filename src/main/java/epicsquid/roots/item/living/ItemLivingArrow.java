package epicsquid.roots.item.living;

import epicsquid.mysticallib.item.ItemArrowBase;
import epicsquid.roots.entity.projectile.EntityLivingArrow;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLivingArrow extends ItemArrowBase {
  public ItemLivingArrow(String name) {
    super(name);
    BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorProjectileDispense() {
      @Override
      protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
        EntityLivingArrow entitytippedarrow = new EntityLivingArrow(worldIn, position.getX(), position.getY(), position.getZ());
        entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
        return entitytippedarrow;
      }
    });
  }

  @Override
  public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
    EntityLivingArrow arrow = new EntityLivingArrow(worldIn, shooter);
    arrow.setPotionEffect(stack);
    arrow.setDamage(4.0D);
    return arrow;
  }
}
