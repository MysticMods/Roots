package mysticmods.roots.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class WildfireParticle extends TextureSheetParticle {

  protected WildfireParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 10;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
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

      f *= f;

      this.quadSize *= 1.0f - f;
    }
  }

  public static TextureSheetParticle createWildfireParticle (SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    return new WildfireParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }
}
