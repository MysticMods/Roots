package mysticmods.roots.client.particle.world;

import mysticmods.roots.blockentity.template.MeanderTarget;
import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

public class MeanderTowardsParticle extends RootsParticle {
  private final double xDest;
  private final double yDest;
  private final double zDest;
  private final int meanderFrequency;
  private final double meanderAmount;
  private Vec3 wander = Vec3.ZERO;

  private static final double STEER = 0.15;
  private static final double WANDER_DECAY = 0.92;
  private static final double MIN_SPEED = 0.02;

  protected MeanderTowardsParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double lifetime, double meanderFrequency, double meanderAmount, int c1, int c2) {
    super(level, x, y, z, 0, 0, 0, c1, c2);
    this.lifetime = (int) lifetime;
    this.meanderFrequency = (int) meanderFrequency;
    this.meanderAmount = meanderAmount;
    BlockPos destPos = type.pos();
    if (destPos == null) {
      throw new IllegalStateException("MeanderTowardsParticle must provide a destination");
    }
    Vec3 dest;
    if (level.getBlockEntity(destPos) instanceof MeanderTarget target) {
      dest = target.destination();
    } else {
      dest = Vec3.atCenterOf(destPos);
    }
    this.xDest = dest.x;
    this.yDest = dest.y;
    this.zDest = dest.z;
    recalculateSpeed();
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bCol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.hasPhysics = false;
    this.quadSize = 0.195f;
    this.gravity = 0.01f;
  }

  protected void recalculateSpeed() {
    Vec3 toDest = new Vec3(xDest - x, yDest - y, zDest - z);
    double dist = toDest.length();
    if (dist < 0.1) {
      this.remove();
      return;
    }

    double t = Math.min(1.0, (double) this.age / (double) this.lifetime);
    double f = t * t;
    double z = 1.0 - f;

    if (meanderFrequency > 0 && this.age % meanderFrequency == 0) {
      wander = new Vec3(random.nextGaussian(), random.nextGaussian(), random.nextGaussian()).normalize()
          .scale(meanderAmount * z);
    } else {
      wander = wander.scale(WANDER_DECAY);
    }

    double wanderWeight = 1.0 - f;
    Vec3 heading = toDest.normalize().add(wander.scale(wanderWeight));
    if (heading.lengthSqr() < 1.0E-7) {
      heading = toDest.normalize();
    }

    double speed = Math.min(dist, MIN_SPEED + 0.07 * dist * f);
    Vec3 desired = heading.normalize().scale(speed);

    Vec3 smoothed = new Vec3(xd, yd, zd).lerp(desired, STEER);
    this.xd = smoothed.x;
    this.yd = smoothed.y;
    this.zd = smoothed.z;
  }


  @Override
  public void tick() {
    recalculateSpeed();
    if (this.removed) {
      return;
    }
    super.tick();
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.DELAYED_TRANSLUCENT;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new MeanderTowardsParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
