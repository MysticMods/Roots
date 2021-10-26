package mysticmods.roots.events.forge;

import mysticmods.roots.Roots;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class HoeHandler {
  @SubscribeEvent
  public static void onHoeUse(UseHoeEvent event) {
    World world = event.getPlayer().level;
    ItemUseContext pContext = event.getContext();
    BlockPos blockpos = pContext.getClickedPos();
    FluidState fluidstate = world.getFluidState(blockpos.above());
    if (pContext.getClickedFace() != Direction.DOWN && fluidstate.is(FluidTags.WATER) && fluidstate.getAmount() == 8) {
      BlockState blockstate = world.getBlockState(blockpos).getToolModifiedState(world, blockpos, pContext.getPlayer(), pContext.getItemInHand(), net.minecraftforge.common.ToolType.HOE);
      if (blockstate != null) {
        PlayerEntity playerentity = pContext.getPlayer();
        world.playSound(playerentity, blockpos, SoundEvents.HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
        if (!world.isClientSide()) {
          world.setBlock(blockpos, blockstate, 11);
        }
        event.setResult(Event.Result.ALLOW);
      }
    }
  }
}
