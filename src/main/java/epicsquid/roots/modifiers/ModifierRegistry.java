package epicsquid.roots.modifiers;

import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.*;

// TODO:
// - Don't worry about clearing entity data
// - Finalise and prevent modifiers from functioning if configured so in CraftTweaker
// - CraftTweaker ability to adjust modifier costs and amplifications
// - CraftTweaker remove the ability to change the costs of spells
// - setFireModifier, setPeacefulModifier, etc
// - Standard helper functions fire(), slow(), paralyse()
// - Standard function to storeEntityData modifier per-modifier/per-spell

public class ModifierRegistry {
  private static final Map<ResourceLocation, Modifier> map = new HashMap<>();
  private static final Set<ResourceLocation> disabledModifiers = new HashSet<>();

  @Nullable
  public static Modifier get(Modifier modifier) {
    if (modifier == null) {
      return null;
    }
    return map.get(modifier.getRegistryName());
  }

  @Nullable
  public static Modifier get(ResourceLocation name) {
    return map.get(name);
  }

  public static void disable(IModifier modifier) {
    if (modifier == null) {
      return;
    }

    disabledModifiers.add(modifier.getRegistryName());
  }

  public static boolean isDisabled(IModifier modifier) {
    if (modifier == null) {
      return false;
    }

    return disabledModifiers.contains(modifier.getRegistryName());
  }

  public static Modifier register(Modifier modifier) {
    ResourceLocation registryName = modifier.getRegistryName();
    //noinspection ConstantConditions
    if (registryName == null) {
      throw new IllegalStateException("Modifier being registered has a null registry name.");
    }
    if (map.containsKey(registryName)) {
      throw new IllegalStateException("Modifier with registry name " + registryName + " already exists!");
    }
    map.put(registryName, modifier);
    return modifier;
  }

  public static Collection<Modifier> getModifiers() {
    return map.values();
  }

  @Nullable
  public static Modifier fromHashCode(int code) {
    for (Map.Entry<ResourceLocation, Modifier> entry : map.entrySet()) {
      if (entry.getKey().hashCode() == code) {
        return entry.getValue();
      } else {
        Roots.logger.info("Modifier " + entry.getKey() + " hash code is: " + entry.getKey().hashCode());
      }
    }

    return null;
  }

  @Nullable
  public static SpellBase getSpellFromModifier(Modifier mod) {
    for (SpellBase spell : SpellRegistry.getSpells()) {
      if (spell.acceptsModifiers(mod)) {
        return spell;
      }
    }
    return null;
  }
}
