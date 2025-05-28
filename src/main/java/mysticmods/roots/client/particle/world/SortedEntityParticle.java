package mysticmods.roots.client.particle.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class SortedEntityParticle extends SortedParticle{
  protected final Entity entity;
  @Nullable
  protected final LivingEntity living;
  protected SortedEntityParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Entity entity) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.entity = entity;
    if (entity instanceof LivingEntity livingEntity) {
      this.living = livingEntity;
    } else {
      this.living = null;
    }
  }

  protected SortedEntityParticle(ClientLevel level, double x, double y, double z, Entity entity) {
    super(level, x, y, z);
    this.entity = entity;
    if (entity instanceof LivingEntity livingEntity) {
      this.living = livingEntity;
    } else {
      this.living = null;
    }
  }

  @Override
  public void tick() {
    if (!this.removed) {
      if (this.entity == null || this.entity.isRemoved() || (this.living != null && this.living.isDeadOrDying())) {
        this.remove();
      } else {
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
      }
    }
    super.tick();
  }
}
