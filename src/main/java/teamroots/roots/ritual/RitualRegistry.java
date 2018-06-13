package teamroots.roots.ritual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import teamroots.roots.util.ListUtil;

public class RitualRegistry {
	public static Map<String, RitualBase> ritualRegistry = new HashMap<String, RitualBase>();
	
	public static RitualBase ritual_life, ritual_storm, ritual_light, ritual_fire_storm, ritual_regrowth, ritual_windwall, ritual_warden;
	
	public static RitualBase getRitual(List<ItemStack> ingredients){
		for (int i = 0; i < ritualRegistry.size(); i ++){
			RitualBase ritual = ritualRegistry.values().toArray(new RitualBase[ritualRegistry.size()])[i];
			if (ListUtil.stackListsMatch(ingredients, ritual.ingredients)){
				return ritual;
			}
		}
		return null;
	}
	
	public static void init(){
		ritualRegistry.put("ritual_life", ritual_life = new RitualLife("ritual_life",1200,true));
		ritualRegistry.put("ritual_storm", ritual_storm = new RitualStorm("ritual_storm",2400,true));
		ritualRegistry.put("ritual_light", ritual_light = new RitualLight("ritual_light",1200,true));
		ritualRegistry.put("ritual_fire_storm", ritual_fire_storm = new RitualFireStorm("ritual_fire_storm",1200,true));
		ritualRegistry.put("ritual_regrowth", ritual_regrowth = new RitualRegrowth("ritual_regrowth",2400,true));
		ritualRegistry.put("ritual_windwall", ritual_windwall = new RitualWindwall("ritual_windwall",3000,true));
		ritualRegistry.put("ritual_warden", ritual_warden = new RitualWarden("ritual_warden",1200,true));
	}
}
