package epicsquid.roots.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.properties.Property;
import epicsquid.roots.properties.PropertyTable;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".Spells")
@ZenDocAppend({"docs/include/spells.example.md"})
@ZenDocClass("mods." + Roots.MODID + ".Spells")
public class Spells {
  @ZenMethod
  @ZenDocMethod(
      order = 1,
      args = {
          @ZenDocArg(arg = "spellName", info = "the name of the spell (spell_ will be prepended if not provided)")
      }
  )
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
  @ZenDocAppend({"docs/include/spell.example.md"})
  @ZenDocClass("mods." + Roots.MODID + ".Spell")
  public static class Spell {
    private final SpellBase original;

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
    @ZenDocMethod(
        order = 1,
        args = {
            @ZenDocArg(arg = "propertyName", info = "the name of the property (must be a double)"),
            @ZenDocArg(arg = "value", info = "the value to be inserted for this property (must be a double)")
        },
        description = "If the property is not of the double type, an error will occur."
    )
    public Spell setDouble(String propertyName, double value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 2,
        args = {
            @ZenDocArg(arg = "propertyName", info = "the name of the property (must be a float)"),
            @ZenDocArg(arg = "value", info = "the value to be inserted for this property (must be a float)")
        },
        description = "If the property is not of the float type, an error will occur."
    )
    public Spell setFloat(String propertyName, float value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 3,
        args = {
            @ZenDocArg(arg = "propertyName", info = "the name of the property (must be a integer)"),
            @ZenDocArg(arg = "value", info = "the value to be inserted for this property (must be a integer)")
        },
        description = "If the property is not of the integer type, an error will occur."
    )
    public Spell setInteger(String propertyName, int value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 4,
        args = {
            @ZenDocArg(arg = "propertyName", info = "the name of the property (must be a string)"),
            @ZenDocArg(arg = "value", info = "the value to be inserted for this property (must be a string)")
        },
        description = "If the property is not of the string type, an error will occur."
    )
    public Spell setString(String propertyName, String value) {
      return set(propertyName, value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 5,
        args = {
            @ZenDocArg(arg = "value", info = "the new spell cooldown (as an integer in ticks)")
        }
    )
    public Spell setCooldown(int value) {
      return set("cooldown", value);
    }

    @ZenMethod
    @ZenDocMethod(
        order = 6,
        args = {
            @ZenDocArg(arg = "value", info = "the new damage of the spell")
        },
        description = "If the spell does not have a damage property, an error will occur. Consult /roots spells."
    )
    public Spell setDamage(float value) {
      return set("damage", value);
    }


    @ZenMethod
    @ZenDocMethod(
        order = 7,
        args = {
            @ZenDocArg(arg = "herb", info = "the static herb reference found in Herbs (must be an existing cost)"),
            @ZenDocArg(arg = "amount", info = "the double value of the new cost for this herb")
        },
        description = "This cannot be used to add a new cost to a spell, only to modify an existing cost."
    )
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
    @ZenDocMethod(
        order = 8,
        args = {
            @ZenDocArg(arg = "cost", info = "the static cost type found in Costs (must be an existing cost)"),
            @ZenDocArg(arg = "herb", info = "the static herb reference found in Herbs (must be an existing modifier cost)"),
            @ZenDocArg(arg = "amount", info = "the double value of the new cost for this combination of cost type and herb")
        },
        description = "This cannot be used to add a new cost to a spell's modifier, only to modify an existing cost."
    )
    public Spell setModifierCost(Costs.Cost cost, Herbs.Herb herb, double value) {
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
