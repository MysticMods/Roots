package mysticmods.roots.block.entity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.DualTickBlockEntity;
import mysticmods.roots.block.PedestalBlock;
import mysticmods.roots.block.entity.template.BaseBlockEntity;
import mysticmods.roots.event.forge.BlockHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GroveCrafterBlockEntity extends BaseBlockEntity implements DualTickBlockEntity {
  public GroveCrafterBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {

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
  public void serverTick(Level pLevel, BlockPos pPos, BlockState pState) {
    if (isBounded()) {
      // TODO: Caching?!?!
      if (BlockHandler.isDirty(level, getBoundingBox())) {
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

        BlockHandler.clean(level, getBoundingBox());
      }
    }
  }

  @Override
  public void dualTick(Level pLevel, BlockPos pPos, BlockState pState, boolean isClient) {

  }
}
