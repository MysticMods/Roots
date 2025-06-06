package mysticmods.roots.client.particle.world;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;

public class LightParticle extends RootsParticle {

  protected LightParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int c1, int c2) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed, c1, c2);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 20;
    this.alpha = 1f;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.hasPhysics = false;
    this.quadSize = 0.1f;
    this.rollAmount = (random.nextFloat() - 0.5f) * 0.1f;
  }

  @Override
  protected void updateQuadSize(float f) {
    this.quadSize = 0.1f - 0.05f * f * f;
  }

  @Override

  protected void particleTick(float f) {
    if (this.random.nextInt(8) == 0) {
      //this.age++;
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.ADDITIVE;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new LightParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type.color1(), type.color2());
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
