package mysticmods.roots.blockentity.template;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public abstract class BaseBlockEntity extends BlockEntity {
  public BaseBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
    super(type, pos, blockState);
  }

  public static <T extends BlockEntity> void clientTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (pLevel.isClientSide() && pBlockEntity instanceof ClientTickBlockEntity clientBlockEntity) {
      clientBlockEntity.clientTick(pLevel, pPos, pState);
    }
  }

  public static <T extends BlockEntity> void serverTick(Level pLevel, BlockPos pPos, BlockState pState, T pBlockEntity) {
    if (!pLevel.isClientSide() && pBlockEntity instanceof ServerTickBlockEntity serverBlockEntity) {
      serverBlockEntity.serverTick((ServerLevel) pLevel, pPos, pState);
    }
  }

  public void updateViaState() {
    setChanged();
    ClientboundBlockEntityDataPacket packet = getUpdatePacket();
    if (packet == null) {
      return;
    }
    BlockPos pos = getBlockPos();
    ((ServerLevel) level).getServer().getPlayerList()
        .broadcast(null, pos.getX(), pos.getY(), pos.getZ(), 64, level.dimension(), packet);
  }

  @Nullable
  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  @Override
  public CompoundTag getUpdateTag(HolderLookup.Provider lookup) {
    CompoundTag pTag = new CompoundTag();
    saveAdditional(pTag, lookup);
    return pTag;
  }
}
