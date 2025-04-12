package mysticmods.roots.api.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record SproutGift(TagKey<EntityType<?>> sproutTag, int chance) {
  public static MapCodec<SproutGift> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.ENTITY_TYPE).fieldOf("sproutTag").forGetter(SproutGift::sproutTag), Codec.INT.fieldOf("chance")
      .forGetter(SproutGift::chance)).apply(instance, SproutGift::new));
  public static Codec<SproutGift> CODEC = MAP_CODEC.codec();
  public static Codec<List<SproutGift>> LIST_CODEC = CODEC.listOf();
}
