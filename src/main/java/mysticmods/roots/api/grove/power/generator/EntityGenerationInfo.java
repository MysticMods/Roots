package mysticmods.roots.api.grove.power.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.grove.Symmetry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record EntityGenerationInfo(TagKey<EntityType<?>> tag, int maxCount,
                                   Symmetry symmetry) implements GenerationInfo<EntityType<?>> {
  public static final MapCodec<EntityGenerationInfo> MAP_CODEC = GenerationInfo.mapCodec(Registries.ENTITY_TYPE, EntityGenerationInfo::new);
  public static final Codec<EntityGenerationInfo> CODEC = MAP_CODEC.codec();
  public static final Codec<List<EntityGenerationInfo>> LIST_CODEC = CODEC.listOf();

  public EntityGenerationInfo(TagKey<EntityType<?>> tag, int maxCount) {
    this(tag, maxCount, Symmetry.NONE);
  }
}
