package epicsquid.roots.config;

import epicsquid.roots.Roots;
import net.minecraftforge.common.config.Config;
import scala.util.control.TailCalls;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Config.LangKey("config.roots.category.grove_crafting")
@Config(modid= Roots.MODID, name = "roots/grove_crafting")
@SuppressWarnings("unused")
public class GroveCraftingConfig {
  @Config.Ignore
  private static Map<Class<?>, Field> RESOLVED_CLASSES = null;

  @Config.Comment({"List of classes and fields that represent crafting containers and BlockPositions", "Use format: <fully resolved class>:<field name>"})
  public static String[] ContainerClassLookup = new String[] {
    "com.aranaira.arcanearchives.inventory.ContainerRadiantCraftingTable:pos"
  };

  public static Map<Class<?>, Field> getClasses () {
    if (RESOLVED_CLASSES == null) {
      RESOLVED_CLASSES = new HashMap<>();
      for (String classPair : ContainerClassLookup) {
        String[] pair = classPair.split(":");
        if (pair.length != 2) {
          Roots.logger.error("Invalid class/field pair specified in configuration: " + classPair);
          continue;
        }

        Class<?> clazz;

        try {
          clazz = Class.forName(pair[0]);
        } catch (ClassNotFoundException e) {
          Roots.logger.error("Invalid class in pair specified in configuration: " + classPair);
          continue;
        }

        Field field;

        try {
          field = clazz.getDeclaredField(pair[1]);
        } catch (NoSuchFieldException e) {
          Roots.logger.error("Invalid field in pair specified in configuration: " + classPair);
          continue;
        }

        field.setAccessible(true);
        RESOLVED_CLASSES.put(clazz, field);
      }
    }
    return RESOLVED_CLASSES;
  }
}
