package mysticmods.roots.api.grove.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.grove.Congen;
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

public record EntityGenerator(TagKey<EntityType<?>> entityTag, TagKey<Grove> tag, EntityTest test,
                              int value) implements Congen {
  public static final MapCodec<EntityGenerator> MAP_CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(TagKey.codec(Registries.ENTITY_TYPE).fieldOf("entityTag")
          .forGetter(EntityGenerator::entityTag), TagKey.codec(RootsRegistries.Keys.GROVES).fieldOf("tag")
          .forGetter(Congen::tag), EntityTest.CODEC.fieldOf("test").forGetter(EntityGenerator::test),
          Codec.INT.fieldOf("value")
          .forGetter(Congen::value)).apply(instance, EntityGenerator::new));
  public static final Codec<EntityGenerator> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, EntityGenerator> STREAM_CODEC = StreamCodec.composite(ExtraStreamCodecs.ENTITY_TAG_STREAM_CODEC, EntityGenerator::entityTag, ExtraStreamCodecs.tagStreamCodec(RootsRegistries.Keys.GROVES), Congen::tag, EntityTest.STREAM_CODEC, EntityGenerator::test, ByteBufCodecs.VAR_INT, Congen::value, EntityGenerator::new);
  public static final Codec<List<EntityGenerator>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, List<EntityGenerator>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public int generate(IGroveInstance grove, Entity entity) {
    if (!grove.asGrove().is(tag)) {
      return 0;
    }

    if (!test().test(entity)) {
      return 0;
    }

    if (value == Integer.MAX_VALUE) {
      return value;
    }

    return value * grove.getRank();
  }
}
