package mysticmods.roots.client.particle.world.emitter;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;

public class SylvanLightEmitter extends NoRenderParticle {
  private static final int[][] COLORS = {
      {0xffe383, 0xffbd83},
      {0xffb4eb, 0x9da2ff},
      {0x9dfff9, 0xadff9d},
      {0xe7ff9d, 0x9db9ff},
      {0xffb69d, 0xff9dc4},
      {0x9dffa6, 0xc1ddff}
  };

  protected int count, interval;

  protected SylvanLightEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
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
    super.tick();

    if (count <= 0) {
      this.remove();
    } else {
      if (this.age % this.interval == 0) {
        count--;
        int[] color = COLORS[level.getRandom().nextInt(COLORS.length)];
        level.addParticle(
            RootsParticleOptions.builder(
                ModParticles.SYLVAN_LIGHT).color(
                color[0],
                color[1]).build(),
            x + (random.nextFloat() - 0.5) * 0.2,
            y + (random.nextFloat() - 0.5) * 0.2,
            z + (random.nextFloat() - 0.5) * 0.2,
            0,
            0,
            0
        );
      }
    }
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {
    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      return new SylvanLightEmitter(level, x, y, z, xSpeed, ySpeed, zSpeed);
    }
  }
}
