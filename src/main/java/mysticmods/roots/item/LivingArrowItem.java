package mysticmods.roots.item;

import mysticmods.roots.entity.projectile.LivingArrowEntity;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class LivingArrowItem extends ArrowItem {
  public LivingArrowItem(Properties properties) {
    super(properties);
  }

  @Override
  public AbstractArrow createArrow(Level level, ItemStack ammo, LivingEntity shooter, @Nullable ItemStack weapon) {
    return new LivingArrowEntity(level, shooter, ammo.copyWithCount(1), weapon);
  }

  @Override
  public Projectile asProjectile(Level level, Position pos, ItemStack stack, Direction direction) {
    LivingArrowEntity arrow = new LivingArrowEntity(level, pos.x(), pos.y(), pos.z(), stack.copyWithCount(1), null);
    arrow.pickup = AbstractArrow.Pickup.ALLOWED;
    return arrow;
  }
}
