package epicsquid.roots.init;

import epicsquid.roots.Roots;
import epicsquid.roots.config.RootsConfig;
import epicsquid.roots.item.ItemBaubleBow;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid= Roots.MODID)
public class BowRegister {
  public static Item baubleBow = null;

  @SubscribeEvent
  public static void onItemRegister (RegistryEvent.Register<Item> event) {
    if (Loader.isModLoaded("baubles") && !RootsConfig.disableBaubleBow) {
      baubleBow = new ItemBaubleBow();
      baubleBow.setRegistryName(new ResourceLocation("bow"));
      event.getRegistry().register(baubleBow);
    }
  }
}
