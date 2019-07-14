package epicsquid.roots.ritual;

import epicsquid.roots.tileentity.TileEntityBonfire;
import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.Map;

public class RitualRegistry {

  public static Map<String, RitualBase> ritualRegistry = new HashMap<>();

  public static RitualBase ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall,
          ritual_warden, ritual_natural_aura, ritual_purity, ritual_frost, ritual_animal_harvest, ritual_summoning,
          ritual_wild_growth, ritual_overgrowth, ritual_flower_growth, ritual_transmutation;

  public static RitualBase getRitual(TileEntityBonfire tileEntity, EntityPlayer player) {
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
      if (ritual.isRitualRecipe(tileEntity, player)) {
        return ritual;
      }
    }
    return null;
  }

  public static RitualBase getRitual(String ritualName) {
    if(ritualName == null){
      return null;
    }
    for (int i = 0; i < ritualRegistry.size(); i++) {
      RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
      if (ritual.getName().equalsIgnoreCase(ritualName)) {
        return ritual;
      }
    }
    return null;
  }

  public static void init() {
    for (EnumRitual ritual : EnumRitual.values())
      if (ritual.isEnabled())
        addRitual(ritual.createRitual());
  }

  public static void addRitual(RitualBase ritual) {
    ritualRegistry.put(ritual.getName(), ritual);
  }

}