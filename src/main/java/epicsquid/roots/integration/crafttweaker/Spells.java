package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.zen.ZenDocClass;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Spells")
@ZenDocClass("mods." + Roots.MODID + ".Spells")
public class Spells {
  @ZenMethod
  public static Spell getSpell(String spellName) {
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

    public <T> Spell set(String propertyName, T value) {
      PropertyTable table = original.getProperties();
      try {
        Property<T> prop = table.get(propertyName, value);
        table.set(prop, value);
      } catch (ClassCastException error) {
        CraftTweakerAPI.logError("Invalid type for property '" + propertyName + "' for Spell " + original.getName(), error);
      }
      return this;
    }

    @ZenMethod
    public Spell setDouble(String propertyName, double value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Spell setFloat(String propertyName, float value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Spell setInteger(String propertyName, int value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Spell setCooldown(int value) {
      return set("cooldownLeft", value);
    }

    @ZenMethod
    public Spell setDamage(float value) {
      return set("damage", value);
    }

    @ZenMethod
    public Spell setString(String propertyName, String value) {
      return set(propertyName, value);
    }

    @ZenMethod
    public Spell setCost(Herbs.Herb herb, double amount) {
      PropertyTable props = original.getProperties();
      try {
        Property<SpellBase.SpellCost> prop = props.get("cost_" + herb.getHerbName());
        if (prop == null) {
          CraftTweakerAPI.logError("Invalid spell cost " + herb.getHerbName() + " for Spell " + original.getName() + ": this is not an existing cost.");
          return this;
        }
        SpellBase.SpellCost newCost = new SpellBase.SpellCost(herb.getHerbName(), amount);
        props.set(prop, newCost);
      } catch (ClassCastException error) {
        CraftTweakerAPI.logError("Invalid spell cost " + herb.getHerbName() + " for Spell " + original.getName() + ". Additional costs cannot be added at this time.", error);
      }
      return this;
    }

    @ZenMethod
    public Spell setModifierCost (Costs.Cost cost, Herbs.Herb herb, double value) {
      PropertyTable props = original.getProperties();
      try {
        Property<SpellBase.ModifierCost> prop = props.get(herb.getHerbName() + "_" + cost.getCostName());
        if (prop == null) {
          CraftTweakerAPI.logError("Invalid modifier cost " + herb.getHerbName() + " with cost type " + cost.getCostName() + ": there is no existing cost for this combination of herb and cost.");
          return this;
        }
        SpellBase.ModifierCost newCost = new SpellBase.ModifierCost(herb.getOriginal(), cost.getOriginal(), value);
        props.set(prop, newCost);
      } catch (ClassCastException error) {
        CraftTweakerAPI.logError("Invalid modifier cost " + herb.getHerbName() + " with cost type " + cost.getCostName() + ": there is no existing cost for this combination of herb and cost. Resulted in error: ", error);
      }
      return this;
    }
  }
}
