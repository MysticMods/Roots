package mysticmods.roots.block.entity;

import mysticmods.roots.RootsTags;
import mysticmods.roots.api.DualTickBlockEntity;
import mysticmods.roots.api.ServerTickBlockEntity;
import mysticmods.roots.block.PedestalBlock;
import mysticmods.roots.block.entity.template.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

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
  public void dualTick(Level pLevel, BlockPos pPos, BlockState pState, boolean isClient) {
    if (isBounded()) {
      // TODO: Caching?!?!
      BlockPos.betweenClosedStream(getBoundingBox()).forEach(pos -> {
        BlockState state = pLevel.getBlockState(pos);
        if (state.is(RootsTags.Blocks.GROVE_PEDESTALS) && !state.getValue(PedestalBlock.VALIDATED)) {
          if (pLevel.getBlockEntity(pos) instanceof PedestalBlockEntity pedestal) {
            if (!pedestal.getHeldItem().isEmpty()) {
              pLevel.setBlock(pos, state.setValue(PedestalBlock.VALIDATED, true), 8);
            }
          }
        }
      });
    }
  }
}
