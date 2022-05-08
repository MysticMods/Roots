package mysticmods.roots.block.entity;

import mysticmods.roots.block.entity.template.UseDelegatedBlockEntity;
import mysticmods.roots.recipe.mortar.MortarInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class MortarBlockEntity extends UseDelegatedBlockEntity {
  private final MortarInventory inventory = new MortarInventory() {
    @Override
    protected void onContentsChanged(int slot) {
      if (MortarBlockEntity.this.hasLevel() && !MortarBlockEntity.this.getLevel().isClientSide()) {
        MortarBlockEntity.this.updateViaState();
      }
    }
  };

  public MortarBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
    super(pType, pWorldPosition, pBlockState);
  }


  @Override
  public CompoundTag getUpdateTag() {
    return null;
  }

  @Override
  public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {

  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
    return null;
  }
}
