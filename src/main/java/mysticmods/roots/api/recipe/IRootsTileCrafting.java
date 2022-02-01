package mysticmods.roots.api.recipe;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

import javax.annotation.Nullable;

public interface IRootsTileCrafting <H extends IItemHandler, T extends TileEntity & IReferentialBlockEntity> extends IRootsCrafting<H> {
  @Nullable
  T getBlockEntity ();
}
