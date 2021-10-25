package epicsquid.mysticallib.proxy;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.event.RegisterFXEvent;
import epicsquid.mysticallib.event.RegisterWorldGenEvent;
import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.mysticallib.particle.ParticleRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;

public class CommonProxy {
  public void preInit(FMLPreInitializationEvent event) {
    LibRegistry.initAll();
    PacketHandler.registerMessages();
    MinecraftForge.EVENT_BUS.post(new RegisterFXEvent());
  }

  public void init(FMLInitializationEvent event) {
    MinecraftForge.EVENT_BUS.post(new RegisterWorldGenEvent());
  }

  public void postInit(FMLPostInitializationEvent event) {
  }

  public void loadComplete (FMLLoadCompleteEvent event) {
  }

  @Nullable
  public EntityPlayer getPlayer () {
    return null;
  }
}
