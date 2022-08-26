package epicsquid.roots;

import epicsquid.roots.capability.grove.IPlayerGroveCapability;
import epicsquid.roots.capability.grove.PlayerGroveCapability;
import epicsquid.roots.capability.grove.PlayerGroveCapabilityStorage;
import epicsquid.roots.capability.playerdata.IPlayerDataCapability;
import epicsquid.roots.capability.playerdata.PlayerDataCapability;
import epicsquid.roots.capability.playerdata.PlayerDataCapabilityStorage;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityStorage;
import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.handler.ConfigHandler;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.integration.botania.PetalApothecaryFiller;
import epicsquid.roots.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

@Mod(modid = Roots.MODID, version = Roots.VERSION, name = Roots.NAME, dependencies = Roots.DEPENDENCIES)
public class Roots {
  public static final String MODID = "roots";
  public static final String DOMAIN = "roots";
  public static final String NAME = "Roots";
  public static final String VERSION = "@VERSION@";
  public static final String DEPENDENCIES = "required-before:mysticallib;required-before:mysticalworld;before:harvest;before:chisel;before:endercore;required:patchouli";

  public static final GuiHandler GUI_HANDLER = new GuiHandler();

  public static ModContainer CONTAINER = null;

  public static Logger logger;

  @SidedProxy(clientSide = "epicsquid.roots.proxy.ClientProxy", serverSide = "epicsquid.roots.proxy.CommonProxy")
  public static CommonProxy proxy;

  @Instance(MODID)
  public static Roots instance;

  public static CreativeTabs tab = new CreativeTabs("roots") {
    @Override
    public String getTabLabel() {
      return "roots";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(ModItems.pestle, 1);
    }
  };

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    CONTAINER = Loader.instance().activeModContainer();
    CapabilityManager.INSTANCE.register(IPlayerGroveCapability.class, new PlayerGroveCapabilityStorage(), PlayerGroveCapability::new);
    CapabilityManager.INSTANCE.register(IPlayerDataCapability.class, new PlayerDataCapabilityStorage(), PlayerDataCapability::new);
    CapabilityManager.INSTANCE.register(RunicShearsCapability.class, new RunicShearsCapabilityStorage(), RunicShearsCapability::new);
    MinecraftForge.EVENT_BUS.register(new RegistryManager());
    MinecraftForge.EVENT_BUS.register(EventManager.class);
    MinecraftForge.EVENT_BUS.register(ConfigHandler.class);
    MinecraftForge.EVENT_BUS.register(PetalApothecaryFiller.class);
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, GUI_HANDLER);
    logger = event.getModLog();
    ModDamage.init();
    proxy.preInit(event);
  }

  public static Roots getInstance() {
    return instance;
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
  public void loadComplete(FMLLoadCompleteEvent event) {
    proxy.loadComplete(event);
  }

  @EventHandler
  public void serverStarting (FMLServerStartingEvent event) {
    proxy.serverStarting(event);
  }
}
