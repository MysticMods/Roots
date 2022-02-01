package mysticmods.roots.api.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

public abstract class RootsTileRecipe<H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity, W extends IRootsTileCrafting<H, T>> extends RootsRecipe<H, W> {
  public RootsTileRecipe(NonNullList<Ingredient> ingredients, ItemStack result, ResourceLocation recipeId) {
    super(ingredients, result, recipeId);
  }
}
