package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config.LangKey("config.roots.category.elemental_soil")
@Config(modid = Roots.MODID, name = "roots/elemental_soil", category = "elemental_soil")
public class ElementalSoilConfig {
	
	@Config.Comment("Maximum Y-level to convert elemental soil into terran soil")
	public static int EarthSoilMaxY = 30;
	@Config.Comment("Minimum Y-level to convert elemental soil into aeros soil")
	public static int AirSoilMinY = 90;
	
	@Config.Comment("The delay you have to wait before the  transmutation takes place (in ticks)")
	public static int WaterSoilDelay = 50;
	@Config.Comment("The delay you have to wait before the transmutation takes place (in ticks)")
	public static int AirSoilDelay = 50;
	@Config.Comment("The delay you have to wait before the transmutation takes place (in ticks)")
	public static int EarthSoilDelay = 50;
	
	@Config.Comment("Whether or not seeds should be skipped for duplication with the earth soil")
	public static boolean EarthSkipSeeds = false;
}
