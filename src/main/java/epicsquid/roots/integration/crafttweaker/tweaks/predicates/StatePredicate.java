package epicsquid.roots.integration.crafttweaker.tweaks.predicates;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.Roots;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods." + Roots.MODID + ".predicates.StatePredicate")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.StatePredicate")
public class StatePredicate implements Predicates.IPredicate {
  private IBlockState state;

  public StatePredicate(IBlockState state) {
    this.state = state;
  }

  @ZenMethod
  @ZenDocMethod(
      order=1,
      args={
          @ZenDocArg(arg="state", info="description of a blockstate against which only blocks themselves (and not state properties) will be compared"),
      }
  )
  public static StatePredicate create (IBlockState state) {
    return new StatePredicate(state);
  }

  @Override
  public epicsquid.roots.recipe.transmutation.StatePredicate get() {
    return new epicsquid.roots.recipe.transmutation.StatePredicate(CraftTweakerMC.getBlockState(state));
  }
}
