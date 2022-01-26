package mysticmods.roots.block.entity.template;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.util.BlockEntityUtil;

import javax.annotation.Nullable;

public abstract class BaseBlockEntity extends TileEntity implements IReferentialBlockEntity {
  public BaseBlockEntity(TileEntityType<?> blockEntityType) {
    super(blockEntityType);
  }

  public void updateViaState() {
    setChanged();
    BlockEntityUtil.updateViaState(this);
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

  @Override
  public TileEntity getBlockEntity() {
    return this;
  }
}
