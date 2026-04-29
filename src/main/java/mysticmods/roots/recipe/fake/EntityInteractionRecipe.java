package mysticmods.roots.recipe.fake;

import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;

public record EntityInteractionRecipe(EntityTest test, Ingredient input, List<ChanceOutput> outputs, int cooldown) {
}
