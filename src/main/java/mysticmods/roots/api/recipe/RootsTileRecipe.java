package mysticmods.roots.api.recipe;

import mysticmods.roots.api.recipe.crafting.IRootsBlockEntityCrafting;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;


public abstract class RootsTileRecipe<H extends IItemHandler, T extends BlockEntity, W extends IRootsBlockEntityCrafting<H, T>> extends RootsRecipe<H, W> {
  public RootsTileRecipe(BaseRecipeData data) {
    super(data);
  }
}
