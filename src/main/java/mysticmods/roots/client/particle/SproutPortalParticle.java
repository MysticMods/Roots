package mysticmods.roots.client.particle;


import mysticmods.roots.client.particle.render.RootsParticleRenderTypes;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class SproutPortalParticle extends PortalParticle {
  protected SproutPortalParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.quadSize = 0.35F * (this.random.nextFloat() * 0.2F + 0.5F);
  }

  public ParticleRenderType getRenderType() {
    return super.getRenderType();
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprite) {
      this.sprite = sprite;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      SproutPortalParticle portalparticle = new SproutPortalParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      portalparticle.pickSprite(this.sprite);
      return portalparticle;
    }
  }
}

