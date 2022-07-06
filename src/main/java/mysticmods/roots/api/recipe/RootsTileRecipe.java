package mysticmods.roots.api.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

public abstract class RootsTileRecipe<H extends IItemHandler, T extends BlockEntity & IReferentialBlockEntity, W extends IRootsTileCrafting<H, T>> extends RootsRecipe<H, W> {
  public RootsTileRecipe(ResourceLocation recipeId) {
    super(recipeId);
  }
}
