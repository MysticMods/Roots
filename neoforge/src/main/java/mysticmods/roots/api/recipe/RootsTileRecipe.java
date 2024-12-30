package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IRootsBlockEntityCrafting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;



public abstract class RootsTileRecipe<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity, W extends IRootsBlockEntityCrafting<H, T>> extends RootsRecipe<H, W> {
  public RootsTileRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }
}
