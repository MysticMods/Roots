package epicsquid.roots.integration.crafttweaker.tweaks.predicates;

import crafttweaker.annotations.ZenRegister;
import epicsquid.roots.Roots;
import epicsquid.roots.recipe.transmutation.*;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocProperty;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenProperty;

@ZenDocClass("mods." + Roots.MODID + ".predicates.Predicates")
@ZenRegister
@ZenDocAppend({"docs/include/transmutation.predicates.example.md"})
@ZenClass("mods." + Roots.MODID + ".predicates.Predicates")
public class Predicates {
  @ZenProperty
  @ZenDocProperty(order=1, description="IPredicate which matches both lava and flowing lava")
  public static final LavaPredicate Lava = new LavaPredicate();
  @ZenProperty
  @ZenDocProperty(order=2, description="IPredicate which matches both water and flowing water")
  public static final WaterPredicate Water = new WaterPredicate();
  @ZenProperty
  @ZenDocProperty(order=3, description="IPredicate which matches all types of leaves")
  public static final LeavesPredicate Leaves = new LeavesPredicate();

  @ZenRegister
  public static class LavaPredicate implements IPredicate {
    public LavaPredicate() {
    }

    @Override
    public epicsquid.roots.recipe.transmutation.LavaPredicate get() {
      return new epicsquid.roots.recipe.transmutation.LavaPredicate();
    }
  }

  @ZenRegister
  public static class LeavesPredicate implements IPredicate {
    public LeavesPredicate() {
    }

    @Override
    public epicsquid.roots.recipe.transmutation.LeavesPredicate get() {
      return new epicsquid.roots.recipe.transmutation.LeavesPredicate();
    }
  }

  @ZenRegister
  public static class WaterPredicate implements IPredicate {
    public WaterPredicate() {
    }

    @Override
    public epicsquid.roots.recipe.transmutation.WaterPredicate get() {
      return new epicsquid.roots.recipe.transmutation.WaterPredicate();
    }
  }

  public interface IPredicate {
    BlockStatePredicate get();
  }

  public interface IWorldPredicate {
    WorldBlockStatePredicate get();
  }
}
