package mysticmods.roots.test.entity;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityTagTest extends EntityTest {
  public static final MapCodec<EntityTagTest> CODEC = TagKey.codec(Registries.ENTITY_TYPE).fieldOf("tag")
      .xmap(EntityTagTest::new, (p_205065_) -> p_205065_.tag);
  public static final StreamCodec<ByteBuf, EntityTagTest> STREAM_CODEC = ExtraStreamCodecs.ENTITY_TAG_STREAM_CODEC.map(EntityTagTest::new, test -> test.tag);

  protected final TagKey<EntityType<?>> tag;
  protected List<EntityType<?>> tagContents = null;

  public EntityTagTest(TagKey<EntityType<?>> tag) {
    this.tag = tag;
  }

  @Override
  public boolean test(Entity entity) {
    return entity.getType().is(tag);
  }

  @Override
  public List<EntityType<?>> getEntityTypes() {
    List<EntityType<?>> tagContents = new ArrayList<>();
    BuiltInRegistries.ENTITY_TYPE.getTag(tag).get().stream().forEach(o -> tagContents.add(o.value()));
    return tagContents;
  }

  @Override
  protected EntityTestType<?> getType() {
    return ModTests.ENTITY_TAG_TEST.get();
  }

  public static class Type implements EntityTestType<EntityTagTest> {
    @Override
    public MapCodec<EntityTagTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, EntityTagTest> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
