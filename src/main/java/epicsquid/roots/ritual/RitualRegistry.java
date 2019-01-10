package epicsquid.roots.ritual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import epicsquid.mysticallib.util.ListUtil;
import epicsquid.roots.ritual.natural.RitualWildGrowth;
import epicsquid.roots.ritual.wild.RitualAnimalHarvest;
import epicsquid.roots.ritual.wild.RitualSummoning;
import net.minecraft.item.ItemStack;

public class RitualRegistry {

  public static Map<String, RitualBase> ritualRegistry = new HashMap<>();

  public static RitualBase ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall,
          ritual_warden, ritual_natural_aura, ritual_purity, ritual_frost, ritual_animal_harvest, ritual_summoning,
          ritual_wild_growth;

  public static RitualBase getRitual(List<ItemStack> ingredients) {
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
      if (ListUtil.stackListsMatch(ingredients, ritual.getIngredients())) {
        return ritual;
      }
    }
    return null;
  }

  public static void init() {
    addRitual(ritual_life = new RitualLife("ritual_life", 1200));
    addRitual(ritual_storm = new RitualStorm("ritual_storm", 2400));
    addRitual(ritual_light = new RitualLight("ritual_light", 1200));
    addRitual(ritual_fire_storm = new RitualFireStorm("ritual_fire_storm", 1200));
    addRitual(ritual_regrowth = new RitualRegrowth("ritual_regrowth", 2400));
    addRitual(ritual_windwall = new RitualWindwall("ritual_windwall", 3000));
    addRitual(ritual_warden = new RitualWarden("ritual_warden", 1200));
    addRitual(ritual_natural_aura = new RitualNaturalAura("ritual_natural_aura", 1200));
    addRitual(ritual_purity = new RitualPurity("ritual_purity", 1200));
    addRitual(ritual_frost = new RitualFrost("ritual_frost", 0));
    addRitual(ritual_animal_harvest = new RitualAnimalHarvest("ritual_animal_harvest", 0));
    addRitual(ritual_summoning = new RitualSummoning("ritual_summoning", 0));
    addRitual(ritual_wild_growth = new RitualWildGrowth("ritual_wild_growth", 0));
  }

  public static void addRitual(RitualBase ritual){
    ritualRegistry.put(ritual.getName(), ritual);
  }

}