package mysticmods.roots.recipe.fake;

import mysticmods.roots.api.recipe.output.ChanceOutput;
import mysticmods.roots.api.test.entity.EntityTest;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record EntityInteractionRecipe (EntityTest test, ItemStack input, List<ChanceOutput> outputs, int cooldown) {
}
