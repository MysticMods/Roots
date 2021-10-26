package mysticmods.roots.events.mod;

import mysticmods.roots.Roots;
import mysticmods.roots.api.herbs.IHerb;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;

@Mod.EventBusSubscriber(modid= Roots.MODID, bus= Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
  public static final ResourceLocation HERB_REGISTRY = new ResourceLocation(Roots.MODID, "herbs");

  @SubscribeEvent
  public static void onRegistryCreation (RegistryEvent.NewRegistry event) {
    new RegistryBuilder<IHerb>()
        .setName(HERB_REGISTRY)
        .setType(IHerb.class)
        .disableOverrides()
        .setMaxID(Integer.MAX_VALUE).create();

    ModRegistries.HERB = RegistryManager.ACTIVE.getRegistry(IHerb.class);
  }
}
