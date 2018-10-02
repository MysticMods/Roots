package epicsquid.roots;

import epicsquid.roots.init.ModItems;
import epicsquid.roots.proxy.CommonProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid = Roots.MODID, version = Roots.VERSION, name = Roots.NAME)
public class Roots {
  public static final String MODID = "roots";
  public static final String DOMAIN = "roots";
  public static final String NAME = "Roots";
  public static final String VERSION = "@VERSION@";

  public static ModContainer CONTAINER = null;

  @SidedProxy(clientSide = "epicsquid.roots.proxy.ClientProxy", serverSide = "epicsquid.roots.proxy.CommonProxy") public static CommonProxy proxy;

  @Instance(MODID) public static Roots instance;

  public static CreativeTabs tab = new CreativeTabs("roots") {
    @Override
    public String getTabLabel() {
      return "roots";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getTabIconItem() {
      return new ItemStack(ModItems.pestle, 1);
    }
  };

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    CONTAINER = Loader.instance().activeModContainer();
    MinecraftForge.EVENT_BUS.register(new RegistryManager());
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
}
