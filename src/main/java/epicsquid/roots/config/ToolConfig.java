package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;

@Config.LangKey("config.roots.category.tools")
@Config(modid = Roots.MODID, name = "roots/tools", category = "tools")
public class ToolConfig {
  @Config.Comment(("Terrastone Pickaxe speed modifier for soft materials"))
  public static float PickaxeSoftModifier = 1.40f;

  @Config.Comment(("Terrastone Pickaxe speed modifier for hard materials"))
  public static float PickaxeHardModifier = 1.90f;

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
}
