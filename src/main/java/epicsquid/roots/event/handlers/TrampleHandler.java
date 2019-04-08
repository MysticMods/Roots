package epicsquid.roots.event.handlers;

import epicsquid.mysticallib.util.Util;
import epicsquid.roots.Roots;
import epicsquid.roots.block.runes.BlockTrample;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

@Mod.EventBusSubscriber(modid=Roots.MODID)
@SuppressWarnings("unused")
public class TrampleHandler {
  @SubscribeEvent
  public static void onTrample (BlockEvent.FarmlandTrampleEvent event) {
    List<BlockPos> nearbyRune = Util.getBlocksWithinRadius(event.getWorld(), event.getPos(), BlockTrample.SAFE_RANGE_X, BlockTrample.SAFE_RANGE_Y, BlockTrample.SAFE_RANGE_Z, ModBlocks.trample_rune);
    System.out.print(String.format("nearbyRune is %d long", nearbyRune.size()));
    if (!nearbyRune.isEmpty()) {
      event.setCanceled(true);
    }
  }
}
