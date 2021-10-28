package mysticmods.roots.blocks.entities;

import mysticmods.roots.blocks.entities.template.BaseBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;

public class DecorativePyreBlockEntity extends BaseBlockEntity  {
  public DecorativePyreBlockEntity(TileEntityType<?> blockEntityType) {
    super(blockEntityType);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return null;
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {

  }
}
