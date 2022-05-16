package mysticmods.roots.event.mod;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.herbs.IHerb;
import mysticmods.roots.init.ModRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegistryHandler {
  public static final ResourceLocation HERB_REGISTRY = new ResourceLocation(RootsAPI.MODID, "herbs");

  @SuppressWarnings("unchecked")
  @SubscribeEvent
  public static void onRegistryCreation(NewRegistryEvent event) {
    event.create(makeRegistry(HERB_REGISTRY, IHerb.class), (registry) -> ModRegistries.HERB_REGISTRY = registry);
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