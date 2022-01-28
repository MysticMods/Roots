package mysticmods.roots.event.mod;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.IHerb;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import noobanidus.libs.noobutil.processor.IProcessor;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
  public static final ResourceLocation HERB_REGISTRY = new ResourceLocation(RootsAPI.MODID, "herbs");
  public static final ResourceLocation PROCESSOR_REGISTRY = new ResourceLocation(RootsAPI.MODID, "processors");

  @SuppressWarnings("unchecked")
  @SubscribeEvent
  public static void onRegistryCreation(RegistryEvent.NewRegistry event) {
    makeRegistry(HERB_REGISTRY, IHerb.class).create();
    makeRegistry(PROCESSOR_REGISTRY, IProcessor.class).create();

    ModRegistries.HERB_REGISTRY = RegistryManager.ACTIVE.getRegistry(IHerb.class);
    ModRegistries.PROCESSOR_REGISTRY = RegistryManager.ACTIVE.getRegistry(IProcessor.class);
  }

  private static <T extends IForgeRegistryEntry<T>> RegistryBuilder<T> makeRegistry(ResourceLocation key, Class<T> type) {
    return new RegistryBuilder<T>()
        .setName(key)
        .setType(type)
        .disableOverrides()
        .disableSaving()
        .setMaxID(Integer.MAX_VALUE);
  }
}
