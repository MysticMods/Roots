package epicsquid.roots.integration.crafttweaker.tweaks.transmutation;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import stanhebben.zenscript.annotations.ZenClass;

@ZenDocClass("mods." + Roots.MODID + ".predicates.WaterPredicate")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.WaterPredicate")
public class WaterPredicate extends Predicate {
  public WaterPredicate() {
  }

  @Override
  public epicsquid.roots.recipe.transmutation.WaterPredicate get() {
    return new epicsquid.roots.recipe.transmutation.WaterPredicate();
  }
}
