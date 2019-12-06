package epicsquid.roots;

import epicsquid.roots.gui.GuiHandler;
import epicsquid.roots.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Logger;

@Mod(Roots.MODID)
public class Roots {
  public static final String MODID = "roots";
  public static final String DOMAIN = "roots";
  public static final String NAME = "Roots";
  public static final String VERSION = "@VERSION@";

  public static final String DEPENDENCIES = "after:maindependencies";

  public static final GuiHandler GUI_HANDLER = new GuiHandler();

  public static Logger logger;

  public static ItemGroup tab = new ItemGroup("roots") {
    @Override
    public String getTabLabel() {
      return "roots";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon() {
      return new ItemStack(ModItems.pestle, 1);
    }
  };

/*  public void preInit(FMLPreInitializationEvent event) {
    // We load before MysticalLib so we can't use an annotation or it will crash
    CapabilityManager.INSTANCE.register(RunicShearsCapability.class, new RunicShearsCapabilityStorage(), RunicShearsCapability::new);
*//*    NetworkRegistry.INSTANCE.registerGuiHandler(instance, GUI_HANDLER);*//*
    logger = event.getModLog();
    ModDamage.init();
  }*/
}
