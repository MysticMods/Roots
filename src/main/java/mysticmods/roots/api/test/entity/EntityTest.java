package mysticmods.roots.api.test.entity;

import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public abstract class EntityTest implements Predicate<Entity> {
  @Override
  public abstract boolean test(Entity entity);

  protected abstract EntityTestType<?> getType();
}
