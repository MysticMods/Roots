package mysticmods.roots.api.test.entity;

import com.mojang.serialization.Codec;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

import java.util.function.Predicate;

public abstract class EntityTest implements Predicate<Entity> {
  public static Codec<EntityTest> CODEC = RootsRegistries.ENTITY_TEST_TYPES.byNameCodec().dispatch(EntityTest::getType, EntityTestType::codec);
  public static StreamCodec<RegistryFriendlyByteBuf, EntityTest> STREAM_CODEC = ByteBufCodecs.registry(RootsRegistries.Keys.ENTITY_TEST_TYPES).dispatch(EntityTest::getType, EntityTestType::streamCodec);

  @Override
  public abstract boolean test(Entity entity);

  protected abstract EntityTestType<?> getType();
}
