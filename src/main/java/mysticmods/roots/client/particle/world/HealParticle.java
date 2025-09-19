package mysticmods.roots.client.particle.world;

import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

public class HealParticle extends RootsParticle {
  protected HealParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.lifetime-- <= 0) {
      this.remove();
    } else {
      this.yd += 0.002;
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.85F;
      this.yd *= 0.85F;
      this.zd *= 0.85F;
    }
  }

  public static class Provider implements ParticleProvider<RootsParticleOptions> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprites) {
      this.sprite = sprites;
    }

    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      HealParticle healParticle = new HealParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      healParticle.pickSprite(this.sprite);
      return healParticle;
    }
  }
}
