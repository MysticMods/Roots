package epicsquid.roots.integration.crafttweaker.tweaks.transmutation;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import stanhebben.zenscript.annotations.ZenClass;

@ZenDocClass("mods." + Roots.MODID + ".predicates.StatePredicate")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.StatePredicate")
public class StatePredicate extends Predicate {
  private IBlockState state;

  public StatePredicate(IBlockState state) {
    this.state = state;
  }

  @Override
  public epicsquid.roots.recipe.transmutation.StatePredicate get() {
    return new epicsquid.roots.recipe.transmutation.StatePredicate(CraftTweakerMC.getBlockState(state));
  }
}
