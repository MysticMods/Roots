package epicsquid.roots.integration.crafttweaker.tweaks.predicates;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods." + Roots.MODID + ".predicates.BlockStateBelow")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.BlockStateBelow")
public class BlockStateBelow implements Predicates.IWorldPredicate {
  private Predicates.IPredicate predicate;

  public BlockStateBelow(Predicates.IPredicate predicate) {
    this.predicate = predicate;
  }

  @ZenMethod
  @ZenDocMethod(
      order=1,
      args = {
          @ZenDocArg(arg = "predicate", info = "a defined predicate that describes (potentially) multiple blockstates")
  })
  public static BlockStateBelow create (Predicates.IPredicate predicate) {
    return new BlockStateBelow(predicate);
  }

  @Override
  public epicsquid.roots.recipe.transmutation.BlockStateBelow get() {
    return new epicsquid.roots.recipe.transmutation.BlockStateBelow(this.predicate.get());
  }
}
