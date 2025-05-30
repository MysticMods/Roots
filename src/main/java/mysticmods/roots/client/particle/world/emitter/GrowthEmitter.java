package mysticmods.roots.client.particle.world.emitter;

import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class GrowthEmitter extends NoRenderParticle {
  private final Vec3[] positions;
  protected int count, interval;

  protected GrowthEmitter(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
    super(level, x, y, z, xSpeed, ySpeed, zSpeed);
    this.lifetime = 10;
    this.interval = 1;
    this.count = 8;
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.gravity = 0f;
    this.hasPhysics = false;
    this.positions = new Vec3[count];
    for (int i = 0; i < count; i++) {
      double progress = random.nextDouble(); // Pick random progress along the spiral
      double angle = progress * Mth.PI * 4;
      double radius = progress * 0.5;

      double xOffset = radius * Math.cos(angle);
      double zOffset = radius * Math.sin(angle);
      double yOffset = y + 0.3 + (random.nextFloat() - 0.5) * 0.1; // Keep some slight vertical variation

      positions[i] = new Vec3(x + xOffset, yOffset, z + zOffset);
    }
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.NO_RENDER;
  }

  @Override
  public void tick() {
    if (count <= 0) {
      this.remove();
    } else {
      if (this.age % this.interval == 0) {
        Vec3 pos = positions[count--];
        level.addParticle(
            RootsParticleOptions.builder(
                ModParticles.GROWTH).color(
                0x248542).build(),
            pos.x,
            pos.y,
            pos.z,
            0,
            0.05,
            0
        );
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
