package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class HoeHandler {
  @SubscribeEvent
  public static void onHoeUse(BlockEvent.BlockToolModificationEvent event) {
    if (event.getToolAction() == ToolActions.HOE_TILL) {
      LevelAccessor world = event.getLevel();
      UseOnContext pContext = event.getContext();
      BlockPos blockpos = pContext.getClickedPos();
      FluidState fluidstate = world.getFluidState(blockpos.above());
      if (pContext.getClickedFace() != Direction.DOWN && fluidstate.is(FluidTags.WATER)) {
        BlockState blockstate = world.getBlockState(blockpos);
        if (blockstate.is(Blocks.GRASS) || blockstate.is(Blocks.DIRT_PATH) || blockstate.is(Blocks.DIRT)) {
          event.setFinalState(Blocks.FARMLAND.defaultBlockState());
        }
      }
    }
  }
}
