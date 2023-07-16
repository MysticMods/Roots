package mysticmods.roots.api.test.entity;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.Registries;
import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class EntityTest implements Predicate<Entity> {
  public static final Supplier<Codec<EntityTest>> CODEC = Suppliers.memoize(() -> Registries.ENTITY_TEST_TYPE.get().getCodec().dispatch("predicate_type", EntityTest::getType, EntityTestType::codec));

  @Override
  public abstract boolean test(Entity entity);

  protected abstract EntityTestType<?> getType ();
}
