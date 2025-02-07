package mysticmods.roots.test.entity;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.init.ModTests;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Arrays;
import java.util.List;

public class EntityTypeTest extends EntityTest {
  public static final MapCodec<EntityTypeTest> CODEC = BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf()
      .fieldOf("types").xmap(EntityTypeTest::new, (p_205065_) -> p_205065_.types);
  public static final StreamCodec<RegistryFriendlyByteBuf, EntityTypeTest> STREAM_CODEC = ByteBufCodecs.registry(Registries.ENTITY_TYPE)
      .apply(ByteBufCodecs.list()).map(EntityTypeTest::new, test -> test.types);

  private final List<EntityType<?>> types;

  public EntityTypeTest(EntityType<?>... types) {
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

  public static class Type implements EntityTestType<EntityTypeTest> {
    @Override
    public MapCodec<EntityTypeTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, EntityTypeTest> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
