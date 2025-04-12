package mysticmods.roots.client.particle;

import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class PyreParticle extends TextureSheetParticle {
  protected float oR1, oG1, oB1;
  protected float rCol2, gCol2, bcol2;
  protected float rotSpeed, spinAcceleration;

  protected PyreParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 40;
    this.rCol = this.oR1 = ((c1 >> 16) & 0xFF) / 255.0f;
    this.gCol = this.oG1 = ((c1 >> 8) & 0xFF) / 255.0f;
    this.bCol = this.oB1 = ((c1) & 0xFF) / 255.0f;
    this.rCol2 = ((c2 >> 16) & 0xFF) / 255.0f;
    this.gCol2 = ((c2 >> 8) & 0xFF) / 255.0f;
    this.bcol2 = ((c2) & 0xFF) / 255.0f;
    this.alpha = 1f;
    this.gravity = gravity;
    this.xd = 0;
    this.yd *= 0.03f;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.2f;
    this.rotSpeed = 0f;
    this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5 : 5);
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
      f *= f;
      if (this.oB1 != this.bcol2) {
        this.rCol = this.oR1 + (this.rCol2 - this.oR1) * f;
        this.gCol = this.oG1 + (this.gCol2 - this.oG1) * f;
        this.bCol = this.oB1 + (this.bcol2 - this.oB1) * f;
      }

      float spinFactor = 1.0f - f;
      spinFactor *= spinFactor;
      f *= f;
      f *= f;

      if (this.spinAcceleration != 0.0f) {
        this.rotSpeed += this.spinAcceleration / 20.0f * spinFactor;
        this.oRoll = this.roll;
        this.roll += this.rotSpeed;
      }

      this.quadSize *= 1.0f - f;
    }
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<ColorGravityParticleOptions> {
    @Override
    public Particle createParticle(ColorGravityParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new PyreParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
