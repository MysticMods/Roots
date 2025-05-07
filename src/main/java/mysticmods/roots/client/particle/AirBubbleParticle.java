package mysticmods.roots.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BubbleParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class AirBubbleParticle extends BubbleParticle {
  protected AirBubbleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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

  public static class Provider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprite;

    public Provider(SpriteSet sprites) {
      this.sprite = sprites;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      BubbleParticle bubbleparticle = new AirBubbleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
      bubbleparticle.pickSprite(this.sprite);
      return bubbleparticle;
    }
  }
}
