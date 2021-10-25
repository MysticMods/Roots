package epicsquid.mysticallib.proxy;

import epicsquid.mysticallib.LibRegistry;
import epicsquid.mysticallib.MysticalLib;
import epicsquid.mysticallib.event.RegisterColorHandlersEvent;
import epicsquid.mysticallib.event.RegisterParticleEvent;
import epicsquid.mysticallib.hax.Hax;
import epicsquid.mysticallib.model.CustomModelLoader;
import epicsquid.mysticallib.particle.ParticleRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nullable;

public class ClientProxy extends CommonProxy {
  public static ParticleRenderer particleRenderer = new ParticleRenderer();

  @Override
  public void preInit(FMLPreInitializationEvent event) {
    super.preInit(event);

    try {
      Hax.init();
    } catch (IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException e) {
      e.printStackTrace();
    }

    LibRegistry.registerEntityRenders();
    OBJLoader.INSTANCE.addDomain(MysticalLib.MODID);
    ModelLoaderRegistry.registerLoader(new CustomModelLoader());
    MinecraftForge.EVENT_BUS.post(new RegisterParticleEvent());
  }

  @Override
  public void init(FMLInitializationEvent event) {
    super.init(event);
    MinecraftForge.EVENT_BUS.post(new RegisterColorHandlersEvent());
  }

  @Override
  public void postInit(FMLPostInitializationEvent event) {
    super.postInit(event);
  }

  @Override
  public void loadComplete(FMLLoadCompleteEvent event) {
    super.loadComplete(event);
  }

  @Nullable
  @Override
  public EntityPlayer getPlayer() {
    Minecraft mc = Minecraft.getMinecraft();
    //noinspection ConstantConditions
    if (mc == null) {
      return null;
    }
    return mc.player;
  }
}
