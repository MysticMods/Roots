package epicsquid.mysticallib;

import epicsquid.mysticallib.proxy.CommonProxy;
import epicsquid.mysticallib.recipe.RecipeRegistry;
import net.minecraft.util.EnumParticleTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = MysticalLib.MODID, version = MysticalLib.VERSION, name = MysticalLib.MODNAME, dependencies = MysticalLib.DEPENDENCIES)
public class MysticalLib {
  public static final Logger logger = LogManager.getLogger();
  public static final String MODID = "mysticallib";
  public static final String VERSION = "@VERSION@";
  public static final String MODNAME = "MysticalLib";
  public static final String FORGE_VERSION = "14.23.5.2847";
  public static final String DEPENDENCIES = "required-after:forge@[" + FORGE_VERSION + " ,);after:*";

  @SidedProxy(clientSide = "epicsquid.mysticallib.proxy.ClientProxy", serverSide = "epicsquid.mysticallib.proxy.CommonProxy") public static CommonProxy proxy;

  @Instance public static MysticalLib INSTANCE;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    MinecraftForge.EVENT_BUS.register(new LibRegistry());
    MinecraftForge.EVENT_BUS.register(new LibEvents());
    MinecraftForge.EVENT_BUS.register(new RecipeRegistry());
    proxy.preInit(event);
  }

  @EventHandler
  public void init(FMLInitializationEvent event) {
    proxy.init(event);
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    proxy.postInit(event);
  }

  @EventHandler
  public void loadComplete (FMLLoadCompleteEvent event) {
    proxy.loadComplete(event);
  }
}
