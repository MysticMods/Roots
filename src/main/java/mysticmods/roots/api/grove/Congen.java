package mysticmods.roots.api.grove;

import net.minecraft.tags.TagKey;

public interface Congen {
  TagKey<Grove> tag();

  int value();
}
