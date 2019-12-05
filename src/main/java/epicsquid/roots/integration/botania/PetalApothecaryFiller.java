package epicsquid.roots.integration.botania;

//@Mod.EventBusSubscriber(modid= Roots.MODID)
public class PetalApothecaryFiller {
/*
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
    for (Direction side : Direction.Plane.HORIZONTAL) {
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
  }*/
}
