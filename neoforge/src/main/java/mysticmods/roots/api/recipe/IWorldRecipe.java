package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IWorldCrafting;
import net.minecraft.world.item.crafting.Recipe;

public interface IWorldRecipe<W extends IWorldCrafting> extends Recipe<W> {
}
