package mysticmods.roots.blocks.entities.template;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.libs.noobutil.util.TileUtil;

import javax.annotation.Nullable;

public abstract class BaseBlockEntity extends TileEntity {
  public BaseBlockEntity(TileEntityType<?> blockEntityType) {
    super(blockEntityType);
  }

  public void updateViaState() {
    TileUtil.updateViaState(this);
  }

  @Nullable
  @Override
  public SUpdateTileEntityPacket getUpdatePacket() {
    return new SUpdateTileEntityPacket(getBlockPos(), 9, getUpdateTag());
  }

  @Override
  public abstract CompoundNBT getUpdateTag();

  @Override
  public abstract void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt);
}
