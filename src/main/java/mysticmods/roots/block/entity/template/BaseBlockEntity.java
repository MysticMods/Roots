package mysticmods.roots.block.entity.template;

import mysticmods.roots.api.BoundedBlockEntity;
import mysticmods.roots.api.ClientTickBlockEntity;
import mysticmods.roots.api.ServerTickBlockEntity;
import mysticmods.roots.event.forge.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import noobanidus.libs.noobutil.block.entities.IReferentialBlockEntity;
import noobanidus.libs.noobutil.util.BlockEntityUtil;

import javax.annotation.Nullable;

public abstract class BaseBlockEntity extends BlockEntity implements IReferentialBlockEntity, BoundedBlockEntity {
  protected BoundingBox boundingBox;

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
  public CompoundTag getUpdateTag() {
    CompoundTag pTag = new CompoundTag();
    saveAdditional(pTag);
    return pTag;
  }

  @Override
  protected void saveAdditional(CompoundTag pTag) {
    super.saveAdditional(pTag);
/*    if (getBaseBounds() != null) {
      BoundingBox.CODEC.encodeStart(NbtOps.INSTANCE, getBaseBounds()).resultOrPartial(RootsAPI.LOG::error).ifPresent(nbt -> pTag.put("base_bounding_box", nbt));
    }*/
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    if (pkt.getTag() != null) {
      CompoundTag pTag = pkt.getTag();
/*      if (pTag.contains("base_bounding_box")) {
        this.clientBaseBounds = BoundingBox.CODEC.parse(NbtOps.INSTANCE, pTag.get("base_bounding_box")).resultOrPartial(RootsAPI.LOG::error).orElse(null);
      }*/
    }
  }

  @Override
  public BlockEntity getBlockEntity() {
    return this;
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (isBounded()) {
      BlockHandler.register(level, getBoundingBox());
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    if (isBounded()) {
      BlockHandler.unregister(level, getBoundingBox());
    }
  }

  @Override
  public void clearRemoved() {
    super.clearRemoved();
    if (isBounded()) {
      BlockHandler.register(level, getBoundingBox());
    }
  }

  @Override
  public BoundingBox getBoundingBox() {
    if (!isBounded()) {
      return null;
    }
    if (boundingBox == null) {
      boundingBox = new BoundingBox(-getRadiusX(), -getRadiusY(), -getRadiusZ(), getRadiusX() + 1, getRadiusY() + 1, getRadiusZ() + 1).move(getBlockPos());
    }
    return boundingBox;
  }

  public static <T extends BlockEntity> void clientTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (pBlockEntity instanceof ClientTickBlockEntity clientBlockEntity) {
      clientBlockEntity.clientTick(pLevel, pPos, pState);
    }
  }

  public static <T extends BlockEntity> void serverTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (pBlockEntity instanceof ServerTickBlockEntity serverBlockEntity) {
      serverBlockEntity.serverTick(pLevel, pPos, pState);
    }
  }
}
