package mysticmods.roots.client.particle;


import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class SproutPortalParticle extends PortalParticle {
  protected SproutPortalParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.quadSize = 0.35F * (this.random.nextFloat() * 0.2F + 0.5F);
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

