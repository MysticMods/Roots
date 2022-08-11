package epicsquid.roots.integration.patchouli;

import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.oredict.OreIngredient;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariableProvider;
import vazkii.patchouli.common.util.ItemStackUtil;

@SuppressWarnings("unused")
public class OredictSpotlightProcessor implements IComponentProcessor {
	
	private Ingredient ingredient;
	
	@Override
	public void setup(IVariableProvider<String> iVariableProvider) {
		ingredient = new OreIngredient(iVariableProvider.get("oredict"));
	}
	
	@Override
	public String process(String s) {
		if (s.equals("input")) {
			return ItemStackUtil.serializeIngredient(ingredient);
		}
		return null;
	}
}
