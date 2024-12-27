package mysticmods.roots.test.entity;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class EntityTypeTest extends EntityTest {
  public static final Codec<EntityTypeTest> CODEC = Registry.ENTITY_TYPE.byNameCodec().listOf().fieldOf("types").xmap(EntityTypeTest::new, (p_205065_) -> p_205065_.types).codec();

  private final List<EntityType<?>> types;

  public EntityTypeTest (EntityType<?> ... types) {
    this.types = Arrays.asList(types);
  }

  public EntityTypeTest(List<EntityType<?>> types) {
    this.types = types;
  }

  @Override
  public boolean test(Entity entity) {
    for (EntityType<?> type : types) {
      if (entity.getType() == type) {
        return true;
      }
    }

    return false;
  }

  @Override
  protected EntityTestType<?> getType() {
    return ModTests.ENTITY_TYPE_TEST.get();
  }
}
