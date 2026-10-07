package mysticmods.roots.api.grove.power.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsTags;
import mysticmods.roots.api.grove.power.GrovePowerBase;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record SimpleEntityGenerator(TagKey<EntityType<?>> entityTag, TagKey<Grove> tag, EntityTest test,
                                    int value) implements GrovePowerBase {
  public static final MapCodec<SimpleEntityGenerator> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(TagKey.codec(Registries.ENTITY_TYPE).fieldOf("entityTag")
          .forGetter(SimpleEntityGenerator::entityTag), TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("tag")
          .forGetter(SimpleEntityGenerator::tag), EntityTest.CODEC.fieldOf("test").forGetter(SimpleEntityGenerator::test),
          Codec.INT.fieldOf("value")
          .forGetter(SimpleEntityGenerator::value)).apply(instance, SimpleEntityGenerator::new));
  public static final Codec<SimpleEntityGenerator> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, SimpleEntityGenerator> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.ENTITY_TAG_STREAM_CODEC, SimpleEntityGenerator::entityTag, ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), SimpleEntityGenerator::tag, EntityTest.STREAM_CODEC, SimpleEntityGenerator::test, ByteBufCodecs.VAR_INT, SimpleEntityGenerator::value, SimpleEntityGenerator::new);
  public static final Codec<List<SimpleEntityGenerator>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, List<SimpleEntityGenerator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public int generate(IGroveInstance grove, Entity entity) {
    if (!test().test(entity)) {
      return 0;
    }

    return value(grove);
  }

  @Override
  public int value(IGroveInstance instance) {
    if (!instance.is(tag)) {
      return 0;
    }

    if (value == Integer.MAX_VALUE) {
      return value;
    }

    if (instance.is(RootsTags.Groves.WILD)) {
      return value;
    }

    return value * instance.getRank();
  }
}
