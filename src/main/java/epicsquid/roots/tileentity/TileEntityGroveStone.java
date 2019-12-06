package epicsquid.roots.tileentity;

import epicsquid.mysticallib.tile.TileBase;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;

// TODO: Currently unused

public class TileEntityGroveStone extends TileBase {


  public TileEntityGroveStone(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public CompoundNBT write(CompoundNBT tag) {
    super.write(tag);
    return tag;
  }

  @Override
  public void read(CompoundNBT tag) {
    super.read(tag);
  }

  @Override
  public CompoundNBT getUpdateTag() {
    return write(new CompoundNBT());
  }

  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getPos(), 0, getUpdateTag());
  }

  @Override
  public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
    read(pkt.getNbtCompound());
  }
}

