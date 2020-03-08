package epicsquid.roots.integration.crafttweaker.tweaks.transmutation;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.WorldPredicate;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import stanhebben.zenscript.annotations.ZenClass;

@ZenDocClass("mods." + Roots.MODID + ".predicates.BlockStateBelow")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.BlockStateBelow")
public class BlockStateBelow extends WorldPredicate<epicsquid.roots.recipe.transmutation.BlockStateBelow> {
  private Predicate<?> predicate;

  public BlockStateBelow(Predicate<?> predicate) {
    this.predicate = predicate;
  }

  @Override
  public epicsquid.roots.recipe.transmutation.BlockStateBelow get() {
    return new epicsquid.roots.recipe.transmutation.BlockStateBelow(this.predicate.get());
  }
}
