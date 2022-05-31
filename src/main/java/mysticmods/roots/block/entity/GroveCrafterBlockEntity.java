package mysticmods.roots.block.entity;

import mysticmods.roots.block.entity.template.BaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class GroveCrafterBlockEntity extends BaseBlockEntity {
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
}
