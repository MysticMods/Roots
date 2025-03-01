package mysticmods.roots.client.particle;

import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class GrowthParticle extends TextureSheetParticle {
  protected double maxY;

  protected GrowthParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.maxY = x + 0.8;
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 20;
    this.rCol = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = ((c1) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = ySpeed;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.2f;
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
    if (!this.removed) {
      float f = (float) this.age / (float) this.lifetime;
/*      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }*/

      f *= f;

/*      // Height control
      if (this.age < 8) {
        this.yd = 0; // Stay at the same height
      } else {
        // Start dropping slowly in the last few ticks
        this.yd -= (0.2 * f) * 0.1f;
      }*/

/*      this.quadSize *= 1.0f - f;*/
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<ColorGravityParticleOptions> {
    @Override
    public Particle createParticle(ColorGravityParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new GrowthParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
