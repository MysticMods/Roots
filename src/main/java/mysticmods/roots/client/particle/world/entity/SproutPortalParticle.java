package mysticmods.roots.client.particle.world.entity;


import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.world.entity.Entity;

import javax.annotation.Nullable;

public class SproutPortalParticle extends PortalParticle {
  protected SproutPortalParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.quadSize = 0.35F * (this.random.nextFloat() * 0.2F + 0.5F);
  }

  public static class EntityBound extends SproutPortalParticle {
    private final int entityId;

    protected EntityBound(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int entityId) {
      super(level, x, y, z, xSpeed, ySpeed, zSpeed);
      this.entityId = entityId;
    }

    @Nullable
    protected Entity getEntity() {
      return level.getEntity(entityId);
    }

    @Override
    public void tick() {
      super.tick();
    }

    @Override
    public void setPos(double x, double y, double z) {
      Entity entity = getEntity();
      if (entity == null || entity.isRemoved()) {
        super.setPos(x, y, z);
        return;
      }

      double targetX = entity.getX();
      double targetY = entity.getY();
      double targetZ = entity.getZ();

      double speed = 0.15;
      double newX = this.x + (targetX - this.x) * speed;
      double newY = this.y + (targetY - this.y) * speed;
      double newZ = this.z + (targetZ - this.z) * speed;

      super.setPos(newX, newY, newZ);
    }
  }

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprite) {
      this.sprite = sprite;
    }

    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = null;
      if (type.entityId() != -1) {
        entity = level.getEntity(type.entityId());
      }
      TextureSheetParticle portalParticle;
      if (entity == null || entity.isRemoved()) {
        portalParticle = new SproutPortalParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      } else {
        portalParticle = new SproutPortalParticle.EntityBound(level, x, y + 0.25, z, xSpeed, ySpeed, zSpeed, type.entityId());
      }
      portalParticle.pickSprite(this.sprite);
      return portalParticle;
    }
  }
}

