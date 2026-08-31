package mysticmods.roots.client.particle.world.block;

import mysticmods.roots.client.particle.world.RootsParticle;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

public class PyreParticle extends RootsParticle {
  protected float rotSpeed, spinAcceleration;

  protected PyreParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, c1, c2);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 40;
    this.alpha = 1f;
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
  protected void updateColour(float f) {
    super.updateColour(f * f);
  }

  @Override
  protected void updateRoll(float f) {
    float spinFactor = 1.0f - f * f;
    spinFactor *= spinFactor;
    if (this.spinAcceleration != 0.0f) {
      this.rotSpeed += this.spinAcceleration / 20.0f * spinFactor;
      this.oRoll = this.roll;
      this.roll += this.rotSpeed;
    }
  }

  @Override
  protected void updateQuadSize(float f) {
    f = f * f * f * f;
    this.quadSize *= 1.0f - f;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new PyreParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
