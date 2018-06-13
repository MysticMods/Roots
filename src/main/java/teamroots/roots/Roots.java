package teamroots.roots;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import teamroots.roots.capability.RootsCapabilityManager;
import teamroots.roots.proxy.CommonProxy;
import teamroots.roots.recipe.RecipeRegistry;

@Mod(modid = Roots.MODID, name = Roots.MODNAME, version = Roots.VERSION)
public class Roots
{
    public static final String MODID = "roots";
    public static final String MODNAME = "Roots";
    public static final String VERSION = "0.104";
	public static final String DEPENDENCIES = "";
	
    @SidedProxy(clientSide = "teamroots.roots.proxy.ClientProxy",serverSide = "teamroots.roots.proxy.ServerProxy")
    public static CommonProxy proxy;
	
	public static CreativeTabs tab = new CreativeTabs("roots") {
    	@Override
    	public String getTabLabel(){
    		return "roots";
    	}
		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem(){
			return new ItemStack(RegistryManager.staff);
		}
	};
	
    @Instance("roots")
    public static Roots instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		MinecraftForge.EVENT_BUS.register(new EventManager());
		MinecraftForge.EVENT_BUS.register(new ConfigManager());
		MinecraftForge.EVENT_BUS.register(new RegistryManager());
		MinecraftForge.EVENT_BUS.register(new RecipeRegistry());
		MinecraftForge.EVENT_BUS.register(new RootsCapabilityManager());
        ConfigManager.init(event.getSuggestedConfigurationFile());
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event){
		proxy.postInit(event);
	}
}
