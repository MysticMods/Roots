package mysticmods.roots.api.grove;

import mysticmods.roots.api.grove.power.consumer.ISimpleGrovePowerConsumer;
import mysticmods.roots.api.registry.RootsRegistries;
import net.minecraft.core.Holder;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroveCache {
  private static TagKey<Grove> lastGrove = null;
  private static List<Grove> lastResult = null;

  public static List<Grove> getAllGroves(TagKey<Grove> grove) {
    if (lastGrove != null && lastGrove.equals(grove) && lastResult != null) {
      return lastResult;
    }

    lastGrove = grove;

    var tag = RootsRegistries.GROVES.getTag(lastGrove).orElse(null);
    if (tag == null) {
      lastResult = Collections.emptyList();
    } else {
      lastResult = tag.stream().map(Holder::value).toList();

    }

    return lastResult;
  }

  public static List<Grove> getAllGroves(ISimpleGrovePowerConsumer consumer) {
    return getAllGroves(consumer.grovePowerTag());
  }

  public static void reset () {
    lastResult = null;
    lastGrove = null;
  }
}
