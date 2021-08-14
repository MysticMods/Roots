package epicsquid.roots.integration.crafttweaker;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
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
@ZenDocAppend({"docs/include/spell.example.md"})
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

}
