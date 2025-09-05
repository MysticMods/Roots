package mysticmods.roots.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

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

  // Copied from Jei
  public static <VALUE, STACK> Optional<TagKey<?>> getTagEquivalent(
      Collection<STACK> stacks,
      Function<STACK, VALUE> stackToValue,
      Supplier<Stream<Pair<TagKey<VALUE>, HolderSet.Named<VALUE>>>> tagSupplier
  ) {
    List<VALUE> values = stacks.stream()
        .map(stackToValue)
        .toList();

    return tagSupplier.get()
        .filter(e -> {
          HolderSet.Named<VALUE> tag = e.getSecond();
          return areEquivalent(tag, values);
        })
        .<TagKey<?>>map(Pair::getFirst)
        .findFirst();
  }

  private static <VALUE> boolean areEquivalent(HolderSet.Named<VALUE> tag, List<VALUE> values) {
    int count = tag.size();
    if (count != values.size()) {
      return false;
    }
    for (int i = 0; i < count; i++) {
      VALUE tagValue = tag.get(i).value();
      VALUE value = values.get(i);
      if (!value.equals(tagValue)) {
        return false;
      }
    }
    return true;
  }
}
