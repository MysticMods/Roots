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
import net.minecraft.world.phys.Vec3;

public abstract class EntityEmitter extends Particle {
  protected final RootsParticleOptions options;
  protected final Entity entity;

  protected EntityEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime) {
    super(level, entity.getX(), entity.getY() + entity.getEyeHeight() - 0.2, entity.getZ());
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
      this.y = entity.getY() + entity.getEyeHeight() - 0.2;
      this.z = entity.getZ();
      this.particleTick();
    }
  }

  public abstract void particleTick();

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

  public static class SkySorarerEmitter extends EntityEmitter {
    private final double chance;
    private final int count;

    protected SkySorarerEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime, int count, double chance) {
      super(level, x, y, z, options, entity, lifetime);
      this.count = count;
      this.chance = chance;
    }

    @Override
    public void particleTick() {
      Vec3 from = new Vec3(xo, yo, zo);
      Vec3 to = new Vec3(x, y, z);
      Vec3 motion = entity.getLookAngle().normalize().scale(-0.3);

      int steps = count;
      for (int i = 0; i < steps; i++) {
        if (random.nextDouble() < chance) {
          double t = (double) i / (steps - 1);
          Vec3 pos = from.lerp(to, t);
          double px = pos.x + (random.nextDouble() - 0.5) * 0.3;
          double py = pos.y + (random.nextDouble() - 0.5) * 0.2;
          double pz = pos.z + (random.nextDouble() - 0.5) * 0.3;

          RootsParticleOptions opt = random.nextBoolean()
              ? new RootsParticleOptions(options.type(), options.color2(), options.color1(), options.entityId())
              : new RootsParticleOptions(options.type(), options.color1(), options.color2(), options.entityId());

          level.addParticle(opt, px, py, pz, motion.x, 0, motion.z);
        }
      }

/*      if (this.age % 10 == 0) {
        for (float angle = 0; angle < 360; angle += 15 + random.nextFloat() * 5) {
          double radians = Math.toRadians(angle);
          double radius = 0.5 + random.nextDouble() * 0.3;

          double offsetX = Math.cos(radians) * radius;
          double offsetZ = Math.sin(radians) * radius;
          double offsetY = (random.nextDouble() - 0.5) * 0.2;

          Vec3 spawnPos = new Vec3(x + offsetX, y + offsetY, z + offsetZ);
          motion = new Vec3(offsetX, offsetY, offsetZ).normalize().scale(0.05 + random.nextDouble() * 0.05);

          RootsParticleOptions opt = random.nextBoolean()
              ? new RootsParticleOptions(ModParticles.SKY_SOARER_PUFF, options.color2(), options.color1(), options.entityId())
              : new RootsParticleOptions(ModParticles.SKY_SOARER_PUFF, options.color1(), options.color2(), options.entityId());

          level.addParticle(opt, spawnPos.x, spawnPos.y, spawnPos.z, motion.x, motion.y, motion.z);
        }
      }*/
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

  public static class SkySoarerProvider implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      return new SkySorarerEmitter(level, x, y, z, new RootsParticleOptions(ModParticles.SKY_SOARER, type.color1(), type.color2(), type.entityId()), entity, (int) xSpeed, (int) ySpeed, zSpeed);
    }
  }
}
