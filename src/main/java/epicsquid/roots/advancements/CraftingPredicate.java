package epicsquid.roots.advancements;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import epicsquid.mysticallib.advancement.IGenericPredicate;
import epicsquid.roots.init.ModRecipes;
import epicsquid.roots.recipe.FeyCraftingRecipe;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class CraftingPredicate implements IGenericPredicate<FeyCraftingRecipe> {
	private FeyCraftingRecipe recipe;
	
	public CraftingPredicate() {
	}
	
	public CraftingPredicate(FeyCraftingRecipe recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public boolean test(EntityPlayerMP player, FeyCraftingRecipe condition) {
		return recipe.equals(condition);
	}
	
	@Override
	public IGenericPredicate<FeyCraftingRecipe> deserialize(@Nullable JsonElement element) {
		if (element != null) {
			JsonObject object = element.getAsJsonObject();
			if (JsonUtils.isString(object, "recipe")) {
				ResourceLocation rl = new ResourceLocation(JsonUtils.getString(object, "recipe"));
				FeyCraftingRecipe recipe = ModRecipes.getFeyCraftingRecipe(rl);
				if (recipe != null) {
					return new CraftingPredicate(recipe);
				} else {
					throw new JsonSyntaxException("Unknown recipe name: '" + rl + "'");
				}
			}
		}
		return new CraftingPredicate(null);
	}
}
