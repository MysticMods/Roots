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

public abstract class EntityEmitter extends Particle {
  protected final RootsParticleOptions options;
  protected final Entity entity;

  protected EntityEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime) {
    super(level, entity.getX(), entity.getY() + entity.getEyeHeight(), entity.getZ());
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.entity = entity;
    this.options = options;
    this.lifetime = lifetime;
  }

  @Override
  public void tick() {
    if (this.entity == null || this.entity.isRemoved()) {
      this.remove();
      return;
    }
    this.xo = this.x;
    this.yo = this.y;
    this.zo = this.z;
    if (this.age++ >= this.lifetime) {
      this.remove();
    } else {
      this.x = entity.getX();
      this.y = entity.getY() + entity.getEyeHeight();
      this.z = entity.getZ();
      this.particleTick();
    }
  }

  public abstract void particleTick ();

  @Override
  public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
  }

  @Override
  public ParticleRenderType getRenderType() {
    return ParticleRenderType.NO_RENDER;
  }

  public static class DisarmEmitter extends EntityEmitter {
    private final double chance;
    private final int count;

    protected DisarmEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime, int count, double chance) {
      super(level, x, y, z, options, entity, lifetime);
      this.count = count;
      this.chance = chance;
    }

    @Override
    public void particleTick() {
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
  }

  public static class DisarmProvider implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      return new DisarmEmitter(level, x, y, z, new RootsParticleOptions(ModParticles.DISARM, type.color1(), type.color2(), type.entityId()), entity, (int) xSpeed, (int) ySpeed, zSpeed);
    }
  }
}
