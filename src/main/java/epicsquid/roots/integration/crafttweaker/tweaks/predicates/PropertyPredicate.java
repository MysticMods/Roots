package epicsquid.roots.integration.crafttweaker.tweaks.predicates;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.block.IBlockState;
import crafttweaker.api.minecraft.CraftTweakerMC;
import epicsquid.roots.Roots;
import epicsquid.roots.util.zen.ZenDocAppend;
import epicsquid.roots.util.zen.ZenDocArg;
import epicsquid.roots.util.zen.ZenDocClass;
import epicsquid.roots.util.zen.ZenDocMethod;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

@ZenDocClass("mods." + Roots.MODID + ".predicates.PropertyPredicate")
@ZenDocAppend({"docs/include/transmutation.propertypredicate.example.md"})
@ZenClass("mods." + Roots.MODID + ".predicates.PropertyPredicate")
@ZenRegister
public class PropertyPredicate implements Predicates.IPredicate {
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
	
	@ZenMethod
	@ZenDocMethod(
			order = 1,
			args = {
					@ZenDocArg(arg = "state", info = "description of a simple blockstate"),
					@ZenDocArg(arg = "properties", info = "a string containing the property name that must match")
			},
			description = "Creates an IPredicate where the specified state is compared against other states, where the block type must match and the values of the specified property names must match."
	)
	public static PropertyPredicate create(IBlockState state, String properties) {
		return new PropertyPredicate(state, new String[]{properties});
	}
	
	@ZenMethod
	@ZenDocMethod(
			order = 2,
			args = {
					@ZenDocArg(arg = "state", info = "description of a simple blockstate"),
					@ZenDocArg(arg = "properties", info = "an array of strings containing property names that must match")
			},
			description = "Creates an IPredicate where the specified state is compared against other states, where the block type must match and the values of all of the specified property names must match."
	)
	public static PropertyPredicate create(IBlockState state, String[] properties) {
		return new PropertyPredicate(state, properties);
	}
	
	@Override
	public epicsquid.roots.recipe.transmutation.PropertyPredicate get() {
		return new epicsquid.roots.recipe.transmutation.PropertyPredicate(CraftTweakerMC.getBlockState(state), properties);
	}
}
