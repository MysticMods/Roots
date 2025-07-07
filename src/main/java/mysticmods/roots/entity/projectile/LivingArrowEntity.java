package mysticmods.roots.entity.projectile;

import mysticmods.roots.init.ModEntities;
import mysticmods.roots.init.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class LivingArrowEntity extends AbstractArrow {
  public static final double GRAVITY_MODIFIER = 0.7; // 30% less gravity
  public static final float VELOCITY_MODIFIER = 1.1f; // 10% more velocity
  public static final float LOWER_CRIT_THRESHOLD = 0.8f; // Only need to charge to 80% for it to be considered a critical hit

  private final ItemStack firingWeapon;

  public LivingArrowEntity(EntityType<? extends LivingArrowEntity> entityType, Level level) {
    super(entityType, level);
    this.firingWeapon = ItemStack.EMPTY;
  }

  public LivingArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
    super(ModEntities.LIVING_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
    this.firingWeapon = firedFromWeapon != null ? firedFromWeapon.copy() : ItemStack.EMPTY;
  }

  public LivingArrowEntity(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
    super(ModEntities.LIVING_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
    this.firingWeapon = firedFromWeapon != null ? firedFromWeapon.copy() : ItemStack.EMPTY;
  }

  @Override
  public ItemStack getWeaponItem() {
    return firingWeapon;
  }

  @Override
  protected ItemStack getDefaultPickupItem() {
    return new ItemStack(ModItems.LIVING_ARROW);
  }

  @Override
  protected double getDefaultGravity() {
    return super.getDefaultGravity() * GRAVITY_MODIFIER; // Reduce gravity
  }

  @Override
  public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
    super.shoot(x, y, z, velocity*VELOCITY_MODIFIER, inaccuracy);
    if (velocity / 3.0f >= LOWER_CRIT_THRESHOLD) {
      this.setCritArrow(true);
    }
  }
}
