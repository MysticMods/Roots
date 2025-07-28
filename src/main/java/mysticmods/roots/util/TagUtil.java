package mysticmods.roots.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class TagUtil {
  @Nullable
  public static <T> T getRandomElement(Level level, TagKey<T> tag) {
    Registry<T> registry = level.registryAccess().registry(tag.registry()).orElse(null);
    if (registry == null) {
      return null;
    }

    HolderSet.Named<T> result = registry.getTag(tag).orElse(null);
    if (result == null) {
      return null;
    }

    Holder<T> value = result.getRandomElement(level.getRandom()).orElse(null);

    if (value == null) {
      return null;
    }

    return value.value();
  }
}
