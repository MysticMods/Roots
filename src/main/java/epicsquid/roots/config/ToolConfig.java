package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

@Config.LangKey("config.roots.category.tools")
@Config(modid = Roots.MODID, name = "roots/tools", category = "tools")
public class ToolConfig {
	@Config.Comment(("Terrastone Pickaxe speed modifier for soft materials (set to 1 to disable)"))
	@Config.RangeDouble(min = 0)
	public static double PickaxeSoftModifier = 0.8;
	
	@Config.Comment(("Terrastone Pickaxe speed modifier for hard materials (set to 1 to disable)"))
	@Config.RangeDouble(min = 0)
	public static double PickaxeHardModifier = 2.80;
	
	@Config.Comment(("Terrastone Axe instantly breaks leaves"))
	public static boolean AxeLeaves = true;
	
	@Config.Comment(("Terrastone Shovel has silk touch on grass, mycelium and podzol by default"))
	public static boolean ShovelSilkTouch = true;
	
	@Config.Comment(("Terrastone Sword instantly breaks cobwebs"))
	public static boolean SwordCobwebBreak = true;
	
	@Config.Comment(("Terrastone Sword silk-touches cobwebs"))
	public static boolean SwordSilkTouch = true;
	
	@Config.Comment(("Terrastone Hoe automatically moisturises created farmland"))
	public static boolean HoeMoisturises = true;
	
	@Config.Comment(("Terrastone Hoe acts like Shears when used on Leaves, etc"))
	public static boolean HoeSilkTouch = true;
	
	@Config.Comment(("List of blocks in the format of modid:blockid that won't be harvested by runic tools"))
	public static String[] RunicBlockBlacklist = new String[]{"minecraft:bedrock"};
	
	@Config.Ignore
	private static Set<Block> runicBlockBlacklist = null;
	
	public static Set<Block> getRunicBlockBlacklist() {
		if (runicBlockBlacklist == null) {
			runicBlockBlacklist = new HashSet<>();
			for (String s : RunicBlockBlacklist) {
				Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(s));
				if (block != null) {
					runicBlockBlacklist.add(block);
				} else {
					Roots.logger.error("Invalid block specified in Runic Block Blacklist configuration: " + s + " does not exist.");
				}
			}
		}
		return runicBlockBlacklist;
	}
}
