package epicsquid.roots.integration.crafttweaker.tweaks.transmutation;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import stanhebben.zenscript.annotations.ZenClass;

@ZenDocClass("mods." + Roots.MODID + ".predicates.LeavesPredicate")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.LeavesPredicate")
public class LeavesPredicate extends Predicate {
  public LeavesPredicate() {
  }

  @Override
  public epicsquid.roots.recipe.transmutation.LeavesPredicate get() {
    return new epicsquid.roots.recipe.transmutation.LeavesPredicate();
  }
}
