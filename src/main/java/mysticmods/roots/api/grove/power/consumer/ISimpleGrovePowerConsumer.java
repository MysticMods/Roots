package mysticmods.roots.api.grove.power.consumer;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.grove.GroveCache;
import net.minecraft.tags.TagKey;

import java.util.List;

public interface ISimpleGrovePowerConsumer extends IGrovePowerConsumer {
  int grovePowerRequired();

  TagKey<Grove> grovePowerTag();

  default List<Grove> allGroves () {
    return GroveCache.getAllGroves(this);
  }
}
