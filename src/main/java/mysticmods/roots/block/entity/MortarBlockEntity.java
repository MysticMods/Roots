package mysticmods.roots.block.entity;

import mysticmods.roots.block.entity.template.BaseBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntityType;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;

public class MortarBlockEntity extends BaseBlockEntity {
  public MortarBlockEntity(TileEntityType<?> blockEntityType) {
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
