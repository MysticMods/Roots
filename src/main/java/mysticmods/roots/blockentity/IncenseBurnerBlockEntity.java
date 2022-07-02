package mysticmods.roots.blockentity;

import mysticmods.roots.blockentity.template.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class IncenseBurnerBlockEntity extends BaseBlockEntity {


  public IncenseBurnerBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }


  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
    super.onDataPacket(net, pkt);

  }

}
