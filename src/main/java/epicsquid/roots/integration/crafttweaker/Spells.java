package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.spell.SpellBase;
import epicsquid.roots.spell.SpellRegistry;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenClass("mods." + Roots.MODID + ".")
public class Spells {
  @ZenMethod
  public static Spell getSpell (String spellName) {
    SpellBase spell = SpellRegistry.getSpell(spellName);
    if (spell == null) {
      return null;
    }

    return new Spell(spell);
  }
}
