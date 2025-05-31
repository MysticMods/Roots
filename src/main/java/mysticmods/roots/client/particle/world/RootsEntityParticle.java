package mysticmods.roots.client.particle.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public abstract class RootsEntityParticle extends RootsParticle {
  protected final Entity entity;
  @Nullable
  protected final LivingEntity living;

  protected RootsEntityParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Entity entity) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.entity = entity;
    if (entity instanceof LivingEntity livingEntity) {
      this.living = livingEntity;
    } else {
      this.living = null;
    }
  }

  protected RootsEntityParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Entity entity, int c1, int c2) {
    this(level, x, y, z, xSpeed, ySpeed, zSpeed, entity);
    this.unwrapColor(c1, c2);
  }

  protected RootsEntityParticle(ClientLevel level, double x, double y, double z, Entity entity) {
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
    super.tick();
    if (!this.removed) {
      if (this.entity == null || this.entity.isRemoved() || (this.living != null && this.living.isDeadOrDying())) {
        this.remove();
      }
    }
  }
}
