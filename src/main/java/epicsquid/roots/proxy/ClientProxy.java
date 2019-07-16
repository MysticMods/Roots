package epicsquid.roots.proxy;

import epicsquid.roots.RegistryManager;
import epicsquid.roots.gui.Keybinds;
import epicsquid.roots.tileentity.*;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMortar.class, new TileEntityMortarRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBonfire.class, new TileEntityBonfireRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImbuer.class, new TileEntityImbuerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOffertoryPlate.class, new TileEntityOffertoryPlateRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIncenseBurner.class, new TileEntityIncenseBurnerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWildrootRune.class, new TileEntityWildrootRuneRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFeyCrafter.class, new TileEntityFeyCrafterRenderer());

    Keybinds.init();
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    RegistryManager.registerColorHandlers();
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }
}
