package epicsquid.roots.integration.botania;

import epicsquid.roots.Roots;
import epicsquid.roots.init.ModBlocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.botania.common.block.tile.TileAltar;

import java.util.Random;

@Mod.EventBusSubscriber(modid = Roots.MODID)
public class PetalApothecaryFiller {

  private static boolean hasBotania = false;
  private static boolean flag = false;
  private static Random random;

  public static boolean hasBotania() {
    if (flag) return hasBotania;

    flag = true;
    hasBotania = Loader.isModLoaded("botania");

    return hasBotania;
  }

  public static void getAdjacentApothecary(World world, BlockPos pos) {
    for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
      TileEntity te = world.getTileEntity(pos.offset(side));
      fillApothecary(te, world);
    }
  }

  private static boolean fillApothecary(TileEntity te, World world) {
    if (te instanceof TileAltar) {
      TileAltar apothecary = ((TileAltar) te);

      if (apothecary.isEmpty()) {
        apothecary.setWater(true);
        return true;

//      TODO Sound Effect and/or Particles
//      world.playSound();
//      for (int i = 0; i < 5; i++)
//        ParticleUtil.spawnParticleGlow(world, random.nextFloat(), 1F, random.nextFloat(), 0, 0.05F, 0, 110, 130, 248, 0.75F, 2, 60);
      }
    }
    return false;
  }

  @SubscribeEvent
  public static void rightClickFill(PlayerInteractEvent.RightClickBlock event) {
    if (hasBotania() && event.getItemStack().getItem() == Item.getItemFromBlock(ModBlocks.unending_bowl)) {
      TileEntity te = event.getEntityPlayer().world.getTileEntity(event.getPos());
      if (fillApothecary(te, event.getEntityPlayer().world))
        event.setCanceled(true);
    }
  }
}
