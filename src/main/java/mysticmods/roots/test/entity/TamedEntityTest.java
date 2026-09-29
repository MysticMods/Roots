package mysticmods.roots.test.entity;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.test.entity.EntityTest;
import mysticmods.roots.api.test.entity.EntityTestType;
import mysticmods.roots.init.ModTests;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;

import java.util.List;

public class TamedEntityTest extends EntityTagTest {
  private static final TamedEntityTest INSTANCE = new TamedEntityTest();
  public static final MapCodec<TamedEntityTest> CODEC = MapCodec.unit(TamedEntityTest::getInstance);
  public static final StreamCodec<ByteBuf, TamedEntityTest> STREAM_CODEC = StreamCodec.unit(INSTANCE);

  public TamedEntityTest() {
    super(RootsTags.Entities.TAMEABLE);
  }

  public static TamedEntityTest getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean test(Entity entity) {
    if (entity instanceof TamableAnimal animal) {
      return animal.isTame();
    }

    if (entity instanceof AbstractHorse horse) {
      return horse.isTamed();
    }

    if (!(entity instanceof OwnableEntity ownable)) {
      return false;
    }

    return ownable.getOwnerUUID()  != null;
    // TODO: Support for modded 'tamable' animals that are etc
    // Would have to be service-based
  }

  @Override
  public List<EntityType<?>> getEntityTypes() {
    return List.of();
  }

  @Override
  protected EntityTestType<?> getType() {
    return ModTests.TAMED_ENTITY_TEST.get();
  }

  public static class Type implements EntityTestType<TamedEntityTest> {
    @Override
    public MapCodec<TamedEntityTest> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, TamedEntityTest> streamCodec() {
      return STREAM_CODEC.cast();
    }
  }
}
