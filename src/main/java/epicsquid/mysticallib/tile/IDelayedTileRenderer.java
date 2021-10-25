package epicsquid.mysticallib.tile;

import javax.annotation.Nonnull;

import net.minecraft.tileentity.TileEntity;

public interface IDelayedTileRenderer {

  void renderLater(@Nonnull TileEntity tile, double x, double y, double z, float partialTicks);
}
