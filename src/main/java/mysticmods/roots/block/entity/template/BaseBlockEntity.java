package mysticmods.roots.block.entity.template;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.util.BlockEntityUtil;

import javax.annotation.Nullable;

public abstract class BaseBlockEntity extends BlockEntity implements IReferentialBlockEntity {
  public BaseBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  public void updateViaState() {
    setChanged();
    BlockEntityUtil.updateViaState(this);
  }

  @Nullable
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public abstract CompoundTag getUpdateTag();

  @Override
  public abstract void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt);

  @Override
  public BlockEntity getBlockEntity() {
    return this;
  }

}
