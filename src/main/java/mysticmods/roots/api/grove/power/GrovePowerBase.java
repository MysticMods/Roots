package mysticmods.roots.api.grove.power;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.IGroveInstance;
import net.minecraft.tags.TagKey;

public interface GrovePowerBase {
  TagKey<Grove> tag();

  int value (IGroveInstance instance);
}
