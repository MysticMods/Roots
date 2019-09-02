package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Spell")
public class Spell {
  private SpellBase original;

  public Spell(SpellBase original) {
    this.original = original;
  }

  public SpellBase getOriginal() {
    return original;
  }

  @ZenMethod
  public float getFloat (String propertyName) {
    PropertyTable props = original.getProperties();
    Property<Float> prop = props.getProperty(propertyName);
    return props.getProperty(prop);
  }

  @ZenMethod
  public int getInteger (String propertyName) {
    PropertyTable props = original.getProperties();
    Property<Integer> prop = props.getProperty(propertyName);
    return props.getProperty(prop);
  }

  @ZenMethod
  public String getString (String propertyName) {
    PropertyTable props = original.getProperties();
    Property<String> prop = props.getProperty(propertyName);
    return props.getProperty(prop);
  }
}
