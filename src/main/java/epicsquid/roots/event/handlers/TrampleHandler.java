package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.runes.TrampleBlock;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Roots.MODID)
@SuppressWarnings("unused")
public class TrampleHandler {
  @SubscribeEvent
  public static void onTrample(BlockEvent.FarmlandTrampleEvent event) {
    // TODO: Should be resolved by modifying ModBlocks

    /*List<BlockPos> nearbyRune = Util.getBlocksWithinRadius(event.getWorld(), event.getPos(), TrampleBlock.SAFE_RANGE_X, TrampleBlock.SAFE_RANGE_Y, TrampleBlock.SAFE_RANGE_Z, ModBlocks.trample_rune);
    if (!nearbyRune.isEmpty()) {
      event.setCanceled(true);
    }*/
  }

  // TODO: Replace this with better logic for moisturizing

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onBucketUse(FillBucketEvent event) {
    RayTraceResult result = event.getTarget();
    if (result != null) {
      BlockPos pos = new BlockPos(result.getHitVec());
      World world = event.getWorld();
      BlockState state = world.getBlockState(pos);
      if (state.getBlock() == ModBlocks.trample_rune) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent(priority = EventPriority.HIGHEST)
  public static void onFluidPlaceBlock(BlockEvent.FluidPlaceBlockEvent event) {
    if (event.getOriginalState().getBlock() == ModBlocks.trample_rune) {
      event.setNewState(event.getOriginalState());
    }
  }
}
