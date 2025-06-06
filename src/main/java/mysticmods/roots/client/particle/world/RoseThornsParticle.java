package mysticmods.roots.client.particle.world;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

public class RoseThornsParticle extends RootsParticle {
  protected RoseThornsParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, c1, c2);
    this.lifetime = 31;

    this.alpha = 0.8f;
    this.xd = xSpeed;
    this.yd = ySpeed;
    this.zd = zSpeed;
    this.hasPhysics = false;
    this.oRoll = this.roll = random.nextFloat() * 360f;
    this.rollAmount = random.nextFloat() * 0.1f;
    this.quadSize = 0.095f;
    this.gravity = 0.06f;
    this.defaultColor = false;
  }

  @Override
  protected void particleTick(float f) {
    super.particleTick(f);
    if (f > 0.3f) {
      this.yd *= 0.98f;
    }
  }

  @Override
  protected void updateAlpha(float f) {
    this.alpha = Math.max(0f, 0.8f - f * f);
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.ADDITIVE;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new RoseThornsParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
