package epicsquid.roots.ritual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import epicsquid.mysticallib.util.ListUtil;
import net.minecraft.item.ItemStack;

public class RitualRegistry {

  private static Map<String, RitualBase> ritualRegistry = new HashMap<>();

  public static RitualBase ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall, ritual_warden, ritual_natural_aura, ritual_purity, ritual_frost;

  public static RitualBase getRitual(List<ItemStack> ingredients) {
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
      if (ListUtil.stackListsMatch(ingredients, ritual.ingredients)) {
        return ritual;
      }
    }
    return null;
  }

  public static void init() {
    ritualRegistry.put("ritual_life", ritual_life = new RitualLife("ritual_life", 1200, true));
    ritualRegistry.put("ritual_storm", ritual_storm = new RitualStorm("ritual_storm", 2400, true));
    ritualRegistry.put("ritual_light", ritual_light = new RitualLight("ritual_light", 1200, true));
    ritualRegistry.put("ritual_fire_storm", ritual_fire_storm = new RitualFireStorm("ritual_fire_storm", 1200, true));
    ritualRegistry.put("ritual_regrowth", ritual_regrowth = new RitualRegrowth("ritual_regrowth", 2400, true));
    ritualRegistry.put("ritual_windwall", ritual_windwall = new RitualWindwall("ritual_windwall", 3000, true));
    ritualRegistry.put("ritual_warden", ritual_warden = new RitualWarden("ritual_warden", 1200, true));
    ritualRegistry.put("ritual_natural_aura", ritual_natural_aura = new RitualNaturalAura("ritual_natural_aura", 1200, true));
    ritualRegistry.put("ritual_purity", ritual_purity = new RitualPurity("ritual_purity", 1200, true));
    ritualRegistry.put("ritual_frost", ritual_frost = new RitualFrost("ritual_frost", 0, true));
  }

}