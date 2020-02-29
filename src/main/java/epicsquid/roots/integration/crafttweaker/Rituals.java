package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.ritual.RitualBase;
import epicsquid.roots.ritual.RitualRegistry;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Rituals")
public class Rituals {
  @ZenMethod
  public static Ritual getRitual (String ritualName) {
    if (!ritualName.startsWith("ritual_")) {
      ritualName = "ritual_" + ritualName;
    }
    RitualBase ritual = RitualRegistry.getRitual(ritualName);
    if (ritual == null) {
      return null;
    }

    return new Ritual(ritual);
  }

  @ZenRegister
  @ZenClass("mods." + Roots.MODID + ".Ritual")
  public static class Ritual {
    private RitualBase original;

    public Ritual(RitualBase original) {
      this.original = original;
    }

    public RitualBase getOriginal() {
      return original;
    }

    public <T> Ritual set (String propertyName, T value) {
      PropertyTable table = original.getProperties();
      try {
        Property<T> prop = table.get(propertyName, value);
        table.set(prop, value);
      } catch (ClassCastException error) {
        CraftTweakerAPI.logError("Invalid type for property '" + propertyName + "' for Ritual " + original.getName(), error);
      }
      return this;
    }

    @ZenMethod
    public Ritual setDouble (String propertyName, double value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Ritual setFloat (String propertyName, float value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Ritual setInteger (String propertyName, int value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Ritual setCooldown (int value) {
      return set("cooldown", value);
    }

    @ZenMethod
    public Ritual setDuration (int value) {
      return set("duration", value);
    }

    @ZenMethod
    public Ritual setString (String propertyName, String value) {
      return set(propertyName, value);
    }
  }
}
