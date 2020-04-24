package epicsquid.roots.proxy;

import epicsquid.roots.RegistryManager;
import epicsquid.roots.client.Keybinds;
import epicsquid.roots.client.PatchouliHack;
import epicsquid.roots.entity.player.layer.AquaBubbleRenderer;
import epicsquid.roots.tileentity.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityMortar.class, new TileEntityMortarRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPyre.class, new TileEntityPyreRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImbuer.class, new TileEntityImbuerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityOfferingPlate.class, new TileEntityOfferingPlateRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityIncenseBurner.class, new TileEntityIncenseBurnerRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityFeyCrafter.class, new TileEntityFeyCrafterRenderer());
    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityImposer.class, new TileEntityImposerRenderer());

    Keybinds.init();
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    RegistryManager.registerColorHandlers();
    Minecraft.getMinecraft().getRenderManager().getSkinMap().values().forEach(o -> o.addLayer(new AquaBubbleRenderer(o)));
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }

  @Override
  public void loadComplete(FMLLoadCompleteEvent event) {
    super.loadComplete(event);

    PatchouliHack.init();
  }
}
