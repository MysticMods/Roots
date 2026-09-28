package mysticmods.roots.api.grove.generator;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.grove.Symmetry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Nullable;

public interface GenerationEntry<T> {
  static <T, V extends GenerationEntry<T>> MapCodec<V> mapCodec(ResourceKey<Registry<T>> registry, TriFunction<TagKey<T>, Integer, Symmetry, V> builder) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        TagKey.codec(registry).fieldOf("tag").forGetter(GenerationEntry::tag),
        Codec.INT.fieldOf("max_count").forGetter(GenerationEntry::maxCount),
        Symmetry.CODEC.optionalFieldOf("symmetry", Symmetry.NONE).forGetter(GenerationEntry::symmetry)
    ).apply(instance, builder::apply));
  }

  TagKey<T> tag();

  int maxCount();

  default boolean hasSymmetry() {
    return symmetry() != null;
  }

  @Nullable Symmetry symmetry();
}
