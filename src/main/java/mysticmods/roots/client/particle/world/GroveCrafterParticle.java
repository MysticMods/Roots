package mysticmods.roots.client.particle.world;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

public class GroveCrafterParticle extends RootsParticle {
  protected GroveCrafterParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, 0xffffff, 0xffffff);
    this.speedUpWhenYMotionIsBlocked = true;
    this.lifetime = 25;
    this.alpha = 1f;
    this.xd = xSpeed;
    this.yd = ySpeed;
    this.zd = zSpeed;
    this.friction = 0.98f;
    this.hasPhysics = false;
    this.quadSize = 0.09f;
    this.delayedRender = false;
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
  protected void updateQuadSize(float f) {
    f = f * f * f * f;
    this.quadSize *= 1.0f - f;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new GroveCrafterParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
