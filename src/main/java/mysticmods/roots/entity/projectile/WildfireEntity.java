package mysticmods.roots.entity.projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;

public class WildfireEntity extends AbstractHurtingProjectile {
  protected WildfireEntity(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
    super(entityType, level);
  }
}
