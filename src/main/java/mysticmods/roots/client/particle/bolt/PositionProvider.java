package mysticmods.roots.client.particle.bolt;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.BiFunction;

public interface PositionProvider {
  default Vec3 getStart() {
    return getStart(0);
  }

  default Vec3 getStop() {
    return getStop(0);
  }

  default double lengthSq() {
    Vec3 start = getStart();
    Vec3 stop = getStop();
    return start.distanceToSqr(stop);
  }

  default boolean isDynamic () {
    return true;
  }

  Vec3 getStart(float partialTicks);

  Vec3 getStop(float partialTicks);

  record StaticPositionProvider(Vec3 start, Vec3 stop) implements PositionProvider {
    @Override
    public Vec3 getStart(float partialTicks) {
      return start;
    }

    @Override
    public Vec3 getStop(float partialTicks) {
      return stop;
    }

    @Override
    public boolean isDynamic() {
      return false;
    }
  }

  record SemiEntityPositionProvider(Entity entity, Vec3 stop, BiFunction<Entity, Float, Vec3> provider,
                                    float yOffset) implements PositionProvider {

    @Override
    public Vec3 getStart(float partialTicks) {
      return provider.apply(entity, partialTicks).subtract(0, yOffset, 0);
    }

    @Override
    public Vec3 getStop(float partialTicks) {
      return stop;
    }
  }

  record EntityPositionProvider(Entity entity1, Entity entity2, BiFunction<Entity, Float, Vec3> provider,
                                float yOffset) implements PositionProvider {
    public EntityPositionProvider(Entity entity1, Entity entity2, float yOffset) {
      this(entity1, entity2, Entity::getPosition, yOffset);
    }

    @Override
    public Vec3 getStart(float partialTicks) {
      return provider.apply(entity1, partialTicks).subtract(0, yOffset, 0);
    }

    @Override
    public Vec3 getStop(float partialTicks) {
      return provider.apply(entity2, partialTicks).subtract(0, yOffset, 0);
    }
  }

  static PositionProvider ofEyes(Entity entity, Vec3 stop) {
    return new SemiEntityPositionProvider(entity, stop, Entity::getEyePosition, 0);
  }

  static PositionProvider ofEyes(Entity entity, Vec3 stop, float yOffset) {
    return new SemiEntityPositionProvider(entity, stop, Entity::getEyePosition, yOffset);
  }

  static PositionProvider ofEyes(Entity entity1, Entity entity2) {
    return new EntityPositionProvider(entity1, entity2, Entity::getEyePosition, 0);
  }

  static PositionProvider ofEyes(Entity entity1, Entity entity2, float yOffset) {
    return new EntityPositionProvider(entity1, entity2, Entity::getEyePosition, yOffset);
  }

  static PositionProvider of(Entity entity1, Entity entity2) {
    return new EntityPositionProvider(entity1, entity2, 0);
  }

  static PositionProvider of(Vec3 start, Vec3 stop) {
    return new StaticPositionProvider(start, stop);
  }

  static PositionProvider of(Entity entity1, Entity entity2, float yOffset) {
    return new EntityPositionProvider(entity1, entity2, yOffset);
  }
}
