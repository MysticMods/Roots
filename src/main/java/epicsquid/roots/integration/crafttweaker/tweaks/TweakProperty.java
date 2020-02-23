package epicsquid.roots.integration.crafttweaker.tweaks;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@SuppressWarnings("unchecked")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Property")
public class TweakProperty {
  private Property<?> original;
  private PropertyTable table;

  public TweakProperty(Property<?> original, PropertyTable table) {
    this.original = original;
    this.table = table;
  }

  @ZenMethod
  public <T> T getValue () {
    return (T) table.get(original);
  }

  @ZenMethod
  public <T> T getDefaultValue () {
    return (T) original.getDefaultValue();
  }

  @ZenMethod
  public <T> void setValue (T value) {
    table.set((Property<T>) original, value);
  }
}
