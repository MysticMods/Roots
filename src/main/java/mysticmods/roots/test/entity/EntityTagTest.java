package mysticmods.roots.test.entity;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class EntityTagTest extends EntityTest {
  public static final Codec<EntityTagTest> CODEC = TagKey.codec(Registry.ENTITY_TYPE_REGISTRY).fieldOf("tag").xmap(EntityTagTest::new, (p_205065_) -> p_205065_.tag).codec();

  protected final TagKey<EntityType<?>> tag;

  public EntityTagTest(TagKey<EntityType<?>> tag) {
    this.tag = tag;
  }

  @Override
  public boolean test(Entity entity) {
    return entity.getType().is(tag);
  }

  @Override
  protected EntityTestType<?> getType() {
    return ModTests.ENTITY_TAG_TEST.get();
  }
}
