package mysticmods.roots.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class GeasParticle extends TextureSheetParticle {
  protected GeasParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, 0, 0, 0);
    this.lifetime = 1;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.quadSize = 0.05f;
    this.hasPhysics = false;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    super.tick();
/*    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      f *= f;

      this.quadSize *= 1.0f - f;
    }*/
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new GeasParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
