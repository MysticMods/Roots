package mysticmods.roots.client.particle.emitter;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.entity.Entity;

public class EntityEmitter extends Particle {
  private final RootsParticleOptions options;
  private final Entity entity;
  private final int count;
  private final double chance;

  protected EntityEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime, int count, double chance) {
    super(level, x, y, z);
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.chance = chance;
    this.entity = entity;
    this.options = options;
    this.lifetime = lifetime;
    this.count = count;
  }

  @Override
  public void tick() {
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;

    if (this.age++ >= this.lifetime) {
      this.remove();
    }

    this.x = entity.getX();
    this.y = entity.getY() + entity.getEyeHeight();
    this.z = entity.getZ();

    for (int i = 0; i < count; i++) {
      if (random.nextDouble() < chance) {
        RootsParticleOptions opt;
        if (random.nextBoolean()) {
          opt = new RootsParticleOptions(options.type(), options.color2(), options.color1(), options.entityId());
        } else {
          opt = new RootsParticleOptions(options.type(), options.color1(), options.color2(), options.entityId());
        }
        level.addParticle(opt, x + (random.nextDouble() - 0.5), y + (random.nextDouble() - 0.5), z + (random.nextDouble() - 0.5), (random.nextDouble() - 0.5) * 0.1, (random.nextDouble() - 0.5) * 0.05, (random.nextDouble() - 0.5) * 0.1);
      }
    }
  }

  @Override
  public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.NO_RENDER;
  }

  public static class DisarmProvider implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      return new EntityEmitter(level, x, y, z, new RootsParticleOptions(ModParticles.DISARM, type.color1(), type.color2(), type.entityId()), entity, (int) xSpeed, (int) ySpeed, zSpeed);
    }
  }
}
