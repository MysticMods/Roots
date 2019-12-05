/*package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.types.Property;
import epicsquid.roots.util.types.PropertyTable;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Spells")
public class Spells {
  @ZenMethod
  public static Spell getSpell (String spellName) {
    if (!spellName.startsWith("spell_")) {
      spellName = "spell_" + spellName;
    }
    SpellBase spell = SpellRegistry.getSpell(spellName);
    if (spell == null) {
      return null;
    }

    return new Spell(spell);
  }

  @ZenRegister
  @ZenClass("mods." + Roots.MODID + ".Spell")
  public static class Spell {
    private SpellBase original;

    public Spell(SpellBase original) {
      this.original = original;
    }

    public SpellBase getOriginal() {
      return original;
    }

    @ZenMethod
    public Spell setFloat (String propertyName, float value) {
      PropertyTable props = original.getProperties();
      Property<Float> prop = props.getProperty(propertyName);
      props.setProperty(prop, value);
      return this;
    }

    @ZenMethod
    public Spell putInt (String propertyName, int value) {
      PropertyTable props = original.getProperties();
      Property<Integer> prop = props.getProperty(propertyName);
      props.setProperty(prop, value);
      return this;
    }

    @ZenMethod
    public Spell setCooldown (int value) {
      return putInt("cooldown", value);
    }

    @ZenMethod
    public Spell setDamage (float value) {
      return setFloat("damage", value);
    }

    @ZenMethod
    public Spell putString (String propertyName, String value) {
      PropertyTable props = original.getProperties();
      Property<String> prop = props.getProperty(propertyName);
      props.setProperty(prop, value);
      return this;
    }

    @ZenMethod
    public Spell setCost (int cost, Herbs.Herb herb, double amount) {
      PropertyTable props = original.getProperties();
      Property<SpellBase.SpellCost> prop = props.getProperty("cost_" + cost);
      SpellBase.SpellCost newCost = new SpellBase.SpellCost(herb.getHerbName(), amount);
      props.setProperty(prop, newCost);
      return this;
    }
  }
}*/
