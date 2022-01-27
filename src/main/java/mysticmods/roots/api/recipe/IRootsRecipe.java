package mysticmods.roots.api.recipe;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.crafting.Crafting;

public interface IRootsRecipe <H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity, W extends Crafting<H, T>> extends IBoundlessRecipe<W>, IIngredientStackRecipe<W> {
}
