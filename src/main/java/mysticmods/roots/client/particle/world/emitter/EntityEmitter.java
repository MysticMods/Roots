package mysticmods.roots.client.particle.world.emitter;

import com.mojang.blaze3d.vertex.VertexConsumer;
import mysticmods.roots.client.particle.IParticleHolder;
import mysticmods.roots.client.particle.IParticleTester;
import mysticmods.roots.init.ModParticles;
import mysticmods.roots.particle.RootsParticleOptions;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public abstract class EntityEmitter extends Particle {
  protected final RootsParticleOptions options;
  protected final Entity entity;
  protected final LivingEntity living;

  protected EntityEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime) {
    super(level, entity.getX(), entity.getY() + entity.getEyeHeight() - 0.2, entity.getZ());
    this.xd = 0;
    this.yd = 0;
    this.zd = 0;
    this.entity = entity;
    if (entity instanceof LivingEntity livingEntity) {
      this.living = livingEntity;
    } else {
      this.living = null;
    }
    this.options = options;
    this.lifetime = lifetime;
  }

  @Override
  public void tick() {
    if (this.entity == null || this.entity.isRemoved()) {
      this.remove();
      return;
    }
    if (living != null && living.isDeadOrDying()) {
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
            opt = options.builder().swapColors().build();
          } else {
            opt = options;
          }
          level.addParticle(opt, x + (random.nextDouble() - 0.5), y + (random.nextDouble() - 0.5), z + (random.nextDouble() - 0.5), (random.nextDouble() - 0.5) * 0.1, (random.nextDouble() - 0.5) * 0.05, (random.nextDouble() - 0.5) * 0.1);
        }
      }
    }
  }

  public static class LifeDrainEmitter extends EntityEmitter {
    private final Entity target;

    protected LifeDrainEmitter(ClientLevel level, double x, double y, double z, RootsParticleOptions options, Entity entity, int lifetime, Entity target) {
      super(level, x, y, z, options, entity, lifetime);
      this.target = target;
    }

    @Override
    public void particleTick() {
      for (int i = 0; i < 3; i++) {
        level.addParticle(random.nextBoolean() ? options : options.builder().swapColors().build(), x + (random.nextDouble() - 0.5) * 0.2, y + (random.nextDouble() - 0.5) * 0.2, z + (random.nextDouble() - 0.5) * 0.2, 0, 0, 0);
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
              ? options
              : options.builder().swapColors().build();

          level.addParticle(opt, px, py, pz, motion.x, 0, motion.z);
        }
      }
    }
  }

  public static class LifeDrainProvider implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      Entity target = level.getEntity(type.casterId());
      if (entity == null || target == null) {
        return null;
      }

      IParticleTester tester = (particle) -> (particle instanceof LifeDrainEmitter emitter) && emitter.entity.getId() == type.entityId() && emitter.target.getId() == type.casterId();

      Particle current = ((IParticleHolder) entity).roots_1_21$getParticle(ModParticles.LIFE_DRAIN_EMITTER.get(), tester);
      if (current != null) {
        current.setLifetime((int) xSpeed);
        return null;
      }

      Particle newParticle = new LifeDrainEmitter(level, x, y, z, type.builder().type(ModParticles.LIFE_DRAINED)
          .build(), entity, (int) xSpeed, target);
      ((IParticleHolder) entity).roots_1_21$setParticle(ModParticles.LIFE_DRAIN_EMITTER.get(), newParticle, tester);

      return newParticle;
    }
  }

  public static class DisarmProvider implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      return new DisarmEmitter(level, x, y, z, type.builder().type(ModParticles.DISARM)
          .build(), entity, (int) xSpeed, (int) ySpeed, zSpeed);
    }
  }

  public static class SkySoarerProvider implements ParticleProvider<RootsParticleOptions> {
    @Override
    public Particle createParticle(RootsParticleOptions type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
      Entity entity = level.getEntity(type.entityId());
      if (entity == null) {
        return null;
      }
      return new SkySorarerEmitter(level, x, y, z, type.builder().type(ModParticles.SKY_SOARER)
          .build(), entity, (int) xSpeed, (int) ySpeed, zSpeed);
    }
  }
}
