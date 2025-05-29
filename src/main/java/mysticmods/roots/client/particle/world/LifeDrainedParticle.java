package mysticmods.roots.client.particle.world;

import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class LifeDrainedParticle extends SortedParticle {
  private final Entity entity;
  private Vec3 smoothedTarget;

  protected LifeDrainedParticle(ClientLevel level, double x, double y, double z, int c1, int c2, Entity entity) {
    super(level, x, y, z);
    this.entity = entity;
    this.smoothedTarget = entity.getPosition(RenderTickHandler.getPartialTick());
    this.lifetime = 60;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    calculateMovement();
    this.hasPhysics = false;
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.195f;
    this.gravity = 0.01f;
  }

  protected void calculateMovement() {
    Vec3 start = new Vec3(x, y, z);
    Vec3 end = entity.getPosition(RenderTickHandler.getPartialTick()).add(0, 0.6, 0);
    smoothedTarget = smoothedTarget.lerp(end, 0.3);
    Vec3 diff = smoothedTarget.subtract(start);
    double dist = end.subtract(start).length();
    if (dist < 0.1f) {
      this.remove();
      return;
    }
    double f = Math.pow((double) this.age / (double) this.lifetime, 2);

    Vec3 direction = diff.normalize().scale(0.37f * dist * f);
    Vec3 current = new Vec3(xd, yd, zd);
    Vec3 smoothed = current.lerp(direction, 0.2);
    this.xd = smoothed.x;
    this.yd = smoothed.y;
    this.zd = smoothed.z;
  }

  @Override
  public void tick() {
    calculateMovement();
    super.tick();
  }

  @Override
  protected void updateAlpha(float f) {
    this.alpha = 1f - f * f * f;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      var particle = new LifeDrainedParticle(level, x, y, z, type.color1(), type.color2(), entity);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
