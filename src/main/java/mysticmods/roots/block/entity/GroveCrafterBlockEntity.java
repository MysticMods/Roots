package mysticmods.roots.block.entity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.DualTickBlockEntity;
import mysticmods.roots.api.MonitoringBlockEntity;
import mysticmods.roots.block.PedestalBlock;
import mysticmods.roots.block.entity.template.BaseBlockEntity;
import mysticmods.roots.event.forge.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GroveCrafterBlockEntity extends BaseBlockEntity implements MonitoringBlockEntity {
  public GroveCrafterBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);
  }

  @Override
  public int getRadiusX() {
    return 5;
  }

  @Override
  public int getRadiusY() {
    return 5;
  }

  @Override
  public int getRadiusZ() {
    return 5;
  }

  @Override
  public void notify(ServerLevel pLevel, BlockPos pPos) {
    BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
      BlockState state = pLevel.getBlockState(pos);
      if (state.is(RootsTags.Blocks.GROVE_PEDESTALS) && !state.getValue(PedestalBlock.VALID)) {
        if (pLevel.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
          if (!pedestal.getHeldItem().isEmpty()) {
            pLevel.setBlock(pos, state.setValue(PedestalBlock.VALID, true), 8);
          }
        }
      }
    });
  }
}
