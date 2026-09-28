package mysticmods.roots.api.grove.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.grove.Symmetry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public record EntityGenerationEntry(TagKey<EntityType<?>> tag, int maxCount,
                                    Symmetry symmetry) implements GenerationEntry<EntityType<?>> {
  public static final MapCodec<EntityGenerationEntry> MAP_CODEC = GenerationEntry.mapCodec(Registries.ENTITY_TYPE, EntityGenerationEntry::new);
  public static final Codec<EntityGenerationEntry> CODEC = MAP_CODEC.codec();
  public static final Codec<List<EntityGenerationEntry>> LIST_CODEC = CODEC.listOf();

  public EntityGenerationEntry(TagKey<EntityType<?>> tag, int maxCount) {
    this(tag, maxCount, Symmetry.NONE);
  }
}
