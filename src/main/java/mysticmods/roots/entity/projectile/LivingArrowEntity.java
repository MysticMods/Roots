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
  public LivingArrowEntity(EntityType<? extends LivingArrowEntity> entityType, Level level) {
    super(entityType, level);
  }

  public LivingArrowEntity(Level level, double x, double y, double z, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
    super(ModEntities.LIVING_ARROW.get(), x, y, z, level, pickupItemStack, firedFromWeapon);
  }

  public LivingArrowEntity(Level level, LivingEntity owner, ItemStack pickupItemStack, @Nullable ItemStack firedFromWeapon) {
    super(ModEntities.LIVING_ARROW.get(), owner, level, pickupItemStack, firedFromWeapon);
  }

  @Override
  protected ItemStack getDefaultPickupItem() {
    return new ItemStack(ModItems.LIVING_ARROW);
  }
}
