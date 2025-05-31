package mysticmods.roots.client.particle.world;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.world.phys.Vec3;

public class ChannelTargetCastParticle extends RootsParticle {
  protected boolean bounced = false;
  protected Vec3 stop;

  protected ChannelTargetCastParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 30;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bCol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.stop = new Vec3(xSpeed, ySpeed, zSpeed);
    Vec3 direction = stop.subtract(new Vec3(x, y, z)).normalize().scale(0.3);
    this.xd = direction.x;
    this.yd = direction.y;
    this.zd = direction.z;
    this.hasPhysics = true;
    this.x += (random.nextDouble() - 0.5) * 0.2;
    this.xo = this.x;
    this.y += (random.nextDouble() - 0.5) * 0.2;
    this.yo = this.y;
    this.z += (random.nextDouble() - 0.5) * 0.2;
    this.zo = this.z;
    this.quadSize = 0.25f;
    this.setPos(this.x, this.y, this.z);
  }

  @Override
  public void tick() {
    super.tick();

    if (!this.removed) {
      if (this.getPos().distanceTo(this.stop) < 0.18 && !bounced) {
        Vec3 normal = stop.subtract(this.x, this.y, this.z).normalize();
        Vec3 velocity = new Vec3(this.xd, this.yd, this.zd);

        Vec3 reflection = velocity.subtract(normal.scale(2 * velocity.dot(normal)));

        double spread = 0.05;
        reflection = reflection.add((random.nextDouble() - 0.5) * spread, (random.nextDouble() - 0.5) * spread, (random.nextDouble() - 0.5) * spread);

        this.xd = reflection.x;
        this.yd = reflection.y;
        this.zd = reflection.z;

        double damping = 0.08;
        this.xd *= damping;
        this.yd *= damping;
        this.zd *= damping;

        // Shorten lifetime for quicker dispersal
        this.age = 0;
        this.lifetime = 12;
        this.bounced = true;
      }
    }
  }

  @Override
  protected void updateAlpha(float f) {
    if (bounced) {
      this.alpha = Math.max(1.0f - ((float) this.age / this.lifetime), 0.0f);
    }
  }

  @Override
  protected void updateQuadSize(float f) {
    this.quadSize = Math.max(0.15f, quadSize * (1.0f - f));
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new ChannelTargetCastParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
