package mysticmods.roots.api.grove.power.consumer;

import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;

import java.util.Collections;
import java.util.List;

public interface ISimpleGrovePowerConsumer extends IGrovePowerConsumer {
  int grovePowerRequired();

  TagKey<Grove> grovePowerTag();

  default List<Grove> allGroves () {
    return Cache.getAllGroves(this);
  }

  // TODO: ???
  class Cache {
    private static TagKey<Grove> lastGrove = null;
    private static List<Grove> lastResult = null;

    public static List<Grove> getAllGroves(ISimpleGrovePowerConsumer consumer) {
      if (lastGrove != null && lastGrove.equals(consumer.grovePowerTag()) && lastResult != null) {
        return lastResult;
      }

      lastGrove = consumer.grovePowerTag();

      var tag = RootsRegistries.GROVES.getTag(lastGrove).orElse(null);
      if (tag == null) {
        lastResult = Collections.emptyList();
      } else {
        lastResult = tag.stream().map(Holder::value).toList();

      }

      return lastResult;
    }
  }
}
