package teamroots.roots;

import java.io.File;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigManager {
	public static Configuration config;

	//MOBS
	public static int deerSpawnWeight, sproutSpawnWeight;
	
	//STRUCTURES
	public static int gardenChance, fairyPoolChance, stoneCircleChance, hutChance, barrowChance;
	public static boolean generateLeyMarkers;
	
	//MISC
	public static boolean enableSilliness;

	public static void init(File configFile)
	{
		if(config == null){
			config = new Configuration(configFile);
			load();
		}
	}
	
	public static void load(){
		config.addCustomCategoryComment("mobs", "Settings related to mobs.");
		deerSpawnWeight = config.getInt("deerSpawnWeight", "mobs", 10, 0, 32767, "Configures the spawning frequency of the Deer mob. Higher numbers mean more spawns.");
		sproutSpawnWeight = config.getInt("sproutSpawnWeight", "mobs", 6, 0, 32767, "Configures the spawning frequency of the Sprout mob. Higher numbers mean more spawns.");
		config.addCustomCategoryComment("structures", "Settings related to structures.");
		stoneCircleChance = config.getInt("stoneCircleChance", "structures", 240, 0, 32767, "Configures the generation chance of the Moonlight Circle structure. Higher numbers mean less structures.");
		hutChance = config.getInt("hutChance", "structures", 180, 0, 32767, "Configures the generation chance of the Moonlight Circle structure. Higher numbers mean less structures.");
		barrowChance = config.getInt("barrowChance", "structures", 200, 0, 32767, "Configures the generation chance of the Barrow structure. Higher numbers mean less structures.");
		fairyPoolChance = config.getInt("fairyPoolChance", "structures", 100, 0, 32767, "Configures the generation chance of the Fairy Pool structure. Higher numbers mean less structures.");
		gardenChance = config.getInt("gardenChance", "structures", 160, 0, 32767, "Configures the generation chance of the Garden structure. Higher numbers mean less structures.");
		generateLeyMarkers = config.getBoolean("generateLeyMarkers", "structures", true, "Toggles the generation of Ley Marker structures.");
		config.addCustomCategoryComment("misc", "Uncategorized settings.");
		enableSilliness = config.getBoolean("enableSilliness", "misc", true, "Turns various secret silly features on or off. ;)");
		if (config.hasChanged()){
			config.save();
		}
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
	{
		if(event.getModID().equalsIgnoreCase(Roots.MODID))
		{
			load();
		}
	}
}
