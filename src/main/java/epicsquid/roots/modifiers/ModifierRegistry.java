package epicsquid.roots.modifiers;

import epicsquid.roots.modifiers.modifier.Modifier;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

public class ModifierRegistry {
  private static Map<ResourceLocation, Modifier> map = new HashMap<>();
  private static boolean initialized = false;

  @Nullable
  public static Modifier get (ResourceLocation name) {
    return map.get(name);
  }

  public static void register (Modifier modifier) {
    ResourceLocation registryName = modifier.getRegistryName();
    if (registryName == null) {
      throw new IllegalStateException("Modifier being registered has a null registry name.");
    }
    if (map.containsKey(registryName)) {
      throw new IllegalStateException("Modifier with registry name " + registryName + " already exists!");
    }
    map.put(registryName, modifier);
  }

  public static Collection<Modifier> getModifiers () {
    return map.values();
  }

  public static void initialize () {
    initialized = true;
  }

  public static boolean initialized () {
    return initialized;
  }
}
