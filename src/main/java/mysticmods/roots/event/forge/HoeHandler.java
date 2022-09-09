package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class HoeHandler {
/*  @SubscribeEvent
  public static void onHoeUse(UseHoeEvent event) {
    Level world = event.getPlayer().level;
    UseOnContext pContext = event.getContext();
    BlockPos blockpos = pContext.getClickedPos();
    FluidState fluidstate = world.getFluidState(blockpos.above());
    if (pContext.getClickedFace() != Direction.DOWN && fluidstate.is(FluidTags.WATER)) {
      BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, pContext.getPlayer(), pContext.getItemInHand(), ToolActions.HOE_TILL);
      if (blockstate != null) {
        Player playerentity = pContext.getPlayer();
        world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundSource.BLOCKS, 1.0F, 1.0F);
        if (!world.isClientSide()) {
          world.setBlock(blockpos, blockstate, 11);
        }
        event.setResult(Event.Result.ALLOW);
      }
    }
  }*/
}
