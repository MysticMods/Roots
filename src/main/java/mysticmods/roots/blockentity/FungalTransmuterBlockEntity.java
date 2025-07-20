package mysticmods.roots.blockentity;

import mysticmods.roots.api.blockentity.ClientTickBlockEntity;
import mysticmods.roots.api.blockentity.ServerTickBlockEntity;
import mysticmods.roots.api.grove.IGroveConsumer;
import mysticmods.roots.api.grove.PowerTicket;
import mysticmods.roots.blockentity.template.UseDelegatedBlockEntity;
import mysticmods.roots.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class FungalTransmuterBlockEntity extends UseDelegatedBlockEntity implements ServerTickBlockEntity, ClientTickBlockEntity, IGroveConsumer {
  public FungalTransmuterBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
    super(ModBlockEntities.FUNGAL_TRANSMUTER.get(), pWorldPosition, pBlockState);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult ray, InteractionHand hand, ItemStack stack) {
    return null;
  }

  @Override
  public void clientTick(Level pLevel, BlockPos pPos, BlockState pState) {

  }

  @Override
  public void serverTick(ServerLevel pLevel, BlockPos pPos, BlockState pState) {

  }

  @Override
  public PowerTicket getTicketForTick(long tick) {
    return null;
  }

  @Override
  public boolean wasPoweredLastTick() {
    return false;
  }
}
