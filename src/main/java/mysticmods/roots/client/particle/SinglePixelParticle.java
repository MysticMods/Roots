package mysticmods.roots.client.particle;

import mysticmods.roots.particle.SimpleParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class SinglePixelParticle extends TextureSheetParticle {
  protected SinglePixelParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int r, int g, int b, float gravity) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 70;
    this.rCol = r / 255.0f;
    this.gCol = g / 255.0f;
    this.bCol = b / 255.0f;
    this.alpha = 1f;
    this.gravity = gravity;
    this.xd = 0;
    this.yd *= 0.03f;
    this.zd = 0;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return super.getLightColor(partialTick);
  }

  public record Provider (SpriteSet sprite) implements ParticleProvider<SimpleParticleOptions> {
    @Override
    public Particle createParticle(SimpleParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new SinglePixelParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.r(), type.g(), type.b(), type.gravity());
      // This seems weird
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
