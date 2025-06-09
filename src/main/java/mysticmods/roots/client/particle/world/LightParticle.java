package mysticmods.roots.client.particle.world;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;

public class LightParticle extends RootsParticle {
  private final float quadSizeStart;
  private final boolean large;

  protected LightParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2, boolean large) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, c1, c2);
    this.lifetime = 25;
    if (large) {
      this.lifetime = 5;
    }
    this.alpha = 1f;
    this.xd = 0;
    this.yd = ySpeed;
    this.zd = 0;
    this.hasPhysics = false;
    this.large = large;
    if (large) {
      this.quadSize = this.quadSizeStart = 0.18f;
    } else {
      this.quadSize = this.quadSizeStart = 0.1f;
      this.rollAmount = (random.nextFloat() - 0.5f) * 0.1f;
    }
  }

  @Override
  protected void updateAlpha(float f) {
      super.updateAlpha(f*f);
  }

  @Override
  protected void updateQuadSize(float f) {
    if (quadSizeStart <= 0.1f) {
      this.quadSize = quadSizeStart - 0.05f * f * f * f;
/*    } else {*/
/*      this.quadSize = quadSizeStart + 0.005f * Mth.sin(f * Mth.PI);*/
    }
  }

  @Override

  protected void particleTick(float f) {
    if (this.age > 5) {
      this.yd *= 1.05f;
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.GLOW;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  public record SmallProvider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new LightParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2(), false);
      particle.pickSprite(sprite);
      return particle;
    }
  }

  public record LargeProvider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new LightParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2(), true);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
