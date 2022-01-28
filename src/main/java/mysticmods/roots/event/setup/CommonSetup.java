package mysticmods.roots.event.setup;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.init.ModRecipes;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {
  @SubscribeEvent
  public static void init(FMLCommonSetupEvent event) {
    event.enqueueWork(() -> {
      ModRecipes.Types.register();
    });
  }
}
