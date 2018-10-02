package epicsquid.roots.proxy;

import epicsquid.roots.tileentity.TileEntityBonfire;
import epicsquid.roots.tileentity.TileEntityBonfireRenderer;
import epicsquid.roots.tileentity.TileEntityMortar;
import epicsquid.roots.tileentity.TileEntityMortarRenderer;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMortar.class, new TileEntityMortarRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBonfire.class, new TileEntityBonfireRenderer());
    super.preInit(event);
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }
}
