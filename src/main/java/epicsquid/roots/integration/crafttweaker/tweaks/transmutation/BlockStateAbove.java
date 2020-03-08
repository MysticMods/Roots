package epicsquid.roots.integration.crafttweaker.tweaks.transmutation;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.WorldPredicate;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenDocClass("mods." + Roots.MODID + ".predicates.BlockStateAbove")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.BlockStateAbove")
public class BlockStateAbove extends WorldPredicate {
  private Predicate<?> predicate;

  public BlockStateAbove(Predicate<?> predicate) {
    this.predicate = predicate;
  }

  @ZenMethod
  @ZenDocMethod(order=1,


     )
  public static BlockStateAbove create (Predicate<?> predicate) {
    return new BlockStateAbove(predicate);
  }

  @Override
  public epicsquid.roots.recipe.transmutation.BlockStateAbove get() {
    return new epicsquid.roots.recipe.transmutation.BlockStateAbove(this.predicate.get());
  }
}
