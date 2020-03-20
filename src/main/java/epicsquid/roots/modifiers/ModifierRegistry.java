package epicsquid.roots.modifiers;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ModifierRegistry {
  private static Map<ResourceLocation, Modifier> map = new HashMap<>();

  @Nullable
  public Modifier getModifier (ResourceLocation name) {
    return map.get(name);
  }

  public void registerModifier (Modifier modifier) {
    ResourceLocation registryName = modifier.getRegistryName();
    if (registryName == null) {
      throw new IllegalStateException("Modifier being registered has a null registry name.");
    }
    if (map.containsKey(registryName)) {
      throw new IllegalStateException("Modifier with registry name " + registryName + " already exists!");
    }
    map.put(registryName, modifier);
  }
}
