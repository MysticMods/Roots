package mysticmods.roots.client.particle.emitter;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.ColorGravityParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;

public class GrowthEmitter extends NoRenderParticle {
  protected int count, interval;

  protected GrowthEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.lifetime = 30;
    this.interval = 10;
    this.count = 10;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.gravity = 0f;
    this.hasPhysics = false;
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.NO_RENDER;
  }

  @Override
  public void tick() {
/*    super.tick();*/

    if (count <= 0) {
      this.remove();
    } else {
      if (this.age % this.interval == 0) {
        count--;
/*        level.addParticle(
            new ColorGravityParticleOptions(
                ModParticles.GROWTH,
                color[0],
                color[1],
                0f
            ),
            x + (random.nextFloat() - 0.5) * 0.2,
            y + (random.nextFloat() - 0.5) * 0.2,
            z + (random.nextFloat() - 0.5) * 0.2,
            0,
            0,
            0
        );*/
      }
    }
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new GrowthEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }
  }
}
