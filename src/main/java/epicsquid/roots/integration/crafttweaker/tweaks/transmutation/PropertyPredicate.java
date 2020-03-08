package epicsquid.roots.integration.crafttweaker.tweaks.transmutation;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.Roots;
import epicsquid.roots.integration.crafttweaker.tweaks.transmutation.Predicate;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocClass;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import stanhebben.zenscript.annotations.ZenClass;

import java.util.ArrayList;
import java.util.List;

@ZenDocClass("mods." + Roots.MODID + ".predicates.PropertyPredicate")
@ZenRegister
@ZenClass("mods." + Roots.MODID + ".predicates.PropertyPredicate")
public class PropertyPredicate extends Predicate {
  private IBlockState state;
  private List<IProperty<?>> properties;

  public PropertyPredicate(IBlockState state, String[] properties) {
    this.state = state;
    this.properties = new ArrayList<>();
    BlockStateContainer container = CraftTweakerMC.getBlockState(state).getBlock().getBlockState();
    for (String name : properties) {
      IProperty<?> prop = container.getProperty(name);
      if (prop != null) {
        this.properties.add(prop);
      } else {
        CraftTweakerAPI.logError("Invalid property name '" + name + "' for " + state.toString());
      }
    }

  }

  @Override
  public epicsquid.roots.recipe.transmutation.PropertyPredicate get() {
    return new epicsquid.roots.recipe.transmutation.PropertyPredicate(CraftTweakerMC.getBlockState(state), properties);
  }
}
