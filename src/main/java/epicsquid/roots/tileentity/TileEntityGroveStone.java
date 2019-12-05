package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;

public class TileEntityGroveStone extends TileBase {

  public TileEntityGroveStone() {
    super();
  }

  @Override
  public CompoundNBT writeToNBT(CompoundNBT tag) {
    super.writeToNBT(tag);
    return tag;
  }

  @Override
  public void readFromNBT(CompoundNBT tag) {
    super.readFromNBT(tag);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return writeToNBT(new CompoundNBT());
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    readFromNBT(pkt.getNbtCompound());
  }
}

