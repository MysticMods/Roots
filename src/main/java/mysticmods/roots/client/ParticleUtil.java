package mysticmods.roots.client;

import mysticmods.roots.mixin.AccessorMixinParticleEngine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TrackingEmitter;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.entity.Entity;

import java.util.function.BiFunction;

public class ParticleUtil {

  public static void addTrackingEmitter(Entity entity, ParticleOptions particleType, int count, int emitterLiftime, BiFunction<Entity, Point, Double> offsetCalculator, PropertyDispatch.TriFunction<Entity, Point, Double, Double> speedCalculator) {
    Minecraft minecraft = Minecraft.getInstance();
    ((AccessorMixinParticleEngine)minecraft.particleEngine).getTrackingEmitters().add(new RootsTrackingEmitter(minecraft.level, entity, particleType, emitterLiftime, count, offsetCalculator, speedCalculator));
  }

  public enum Point {
    X,
    Y,
    Z
  }

  public static class RootsTrackingEmitter extends TrackingEmitter {
    private final int count;
    private final BiFunction<Entity, Point, Double> offsetCalculator;
    private final PropertyDispatch.TriFunction<Entity, Point, Double, Double> speedCalculator;

    public RootsTrackingEmitter(ClientLevel level, Entity entity, ParticleOptions particleType, int count) {
      this(level, entity, particleType, 3, count, RootsTrackingEmitter::standardOffset, RootsTrackingEmitter::standardSpeed);
    }

    public static double standardOffset (Entity entity, Point point) {
      return (switch (point) {
        case X -> entity.getX();
        case Y -> entity.getY();
        case Z -> entity.getZ();
      }) + (entity.getRandom().nextDouble() - 0.5) * 0.5;
    }

    public static double standardSpeed (Entity entity, Point point, double offset) {
      return 0;
    }

    public RootsTrackingEmitter(ClientLevel level, Entity entity, ParticleOptions particleType, int lifetime, int count, BiFunction<Entity, Point, Double> offsetCalculator, PropertyDispatch.TriFunction<Entity, Point, Double, Double> speedCalculator) {
      super(level, entity, particleType, lifetime);
      this.count = count;
      this.offsetCalculator = offsetCalculator;
      this.speedCalculator = speedCalculator;
    }

    @Override
    public void tick() {
      for (int i = 0; i < count; i++) {
        double x = offsetCalculator.apply(entity, Point.X);
        double y = offsetCalculator.apply(entity, Point.Y);
        double z = offsetCalculator.apply(entity, Point.Z);
        double sx = speedCalculator.apply(entity, Point.X, x);
        double sy = speedCalculator.apply(entity, Point.Y, y);
        double sz = speedCalculator.apply(entity, Point.Z, z);

        this.level.addParticle(this.particleType, false, x, y, z, sx, sy, sz);
      }

      this.life++;
      if (this.life >= this.lifeTime) {
        this.remove();
      }
    }
  }
}
