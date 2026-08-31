package mysticmods.roots.client.particle.world.spell;

import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import mysticmods.roots.client.particle.world.TextureSheetVelocityParticle;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;

public class WindParticle extends TextureSheetVelocityParticle {
  protected double initX, initY, initZ;

  protected WindParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.lifetime = 80;
    this.rCol = this.gCol = this.bCol = 1.0f;
    this.alpha = 0.4f;
    this.initX = this.xd = xSpeed * 0.5;
    this.initY = this.yd = ySpeed * 0.5;
    this.initZ = this.zd = zSpeed * 0.5;
    this.hasPhysics = false;
    this.quadSize = 0.25f;
    this.gravity = 0.0f;
    this.friction = 1f;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return RootsParticleRenderTypes.GLOW_NO_CULL;
  }

  @Override
  protected int getLightColor(float partialTick) {
    return 0xf000f0 | super.getLightColor(partialTick) & 0xff0000;
  }

  @Override
  public void tick() {
    super.tick();
    float f = (float) this.age / (float) this.lifetime;
    float scale = 1.5f + f * 3.5f;
    this.alpha = Math.max(0f, 0.4f - f);
    this.yd = initY * scale;
    this.xd = initX * scale;
    this.zd = initZ * scale;
  }

  public record Provider(SpriteSet sprite) implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      var particle = new WindParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      particle.pickSprite(sprite);
      return particle;
    }
  }
}
