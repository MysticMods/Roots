package epicsquid.roots;

import epicsquid.roots.capability.life_essence.LifeEssenceCapability;
import epicsquid.roots.capability.life_essence.LifeEssenceCapabilityStorage;
import epicsquid.roots.capability.runic_shears.RunicShearsCapability;
import epicsquid.roots.capability.runic_shears.RunicShearsCapabilityStorage;
import epicsquid.roots.init.ModDamage;
import epicsquid.roots.init.ModItems;
import epicsquid.roots.item.materials.Materials;
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

@Mod(modid = Roots.MODID, version = Tags.VERSION, name = Roots.NAME, dependencies = Roots.DEPENDENCIES)
public class Roots {
	public static final String MODID = "roots";
	public static final String DOMAIN = "roots";
	public static final String NAME = "Roots";
	
	public static final String DEPENDENCIES = "required-before:mysticallib@[1.12.2-1.13,);required-before:mysticalworld@[1.12.2-1.11,);before:harvest;before:chisel;before:endercore;required:patchouli";
	
	public static final GuiHandler GUI_HANDLER = new GuiHandler();
	
	public static ModContainer CONTAINER = null;
	
	public static Logger logger;
	
	static {
		Materials.load();
	}
	
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
		logger = event.getModLog();
		CONTAINER = Loader.instance().activeModContainer();
		// We load before MysticalLib so we can't use an annotation or it will crash
		MinecraftForge.EVENT_BUS.register(RegistryManager.class);
		CapabilityManager.INSTANCE.register(RunicShearsCapability.class, new RunicShearsCapabilityStorage(), RunicShearsCapability::new);
		CapabilityManager.INSTANCE.register(LifeEssenceCapability.class, new LifeEssenceCapabilityStorage(), LifeEssenceCapability::new);
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, GUI_HANDLER);
		
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
		/*    ExportDocumentation.main(new String[]{});*/
		proxy.loadComplete(event);
	}
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		proxy.serverStarting(event);
	}
}
