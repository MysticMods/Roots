package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapsUpdatedEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataHandler {
  @SubscribeEvent
  public static void onDataReloaded(DataMapsUpdatedEvent event) {
    if (event.getRegistryKey() == RootsRegistries.Keys.RITUALS) {
      event.getRegistries().registryOrThrow(RootsRegistries.Keys.RITUALS).holders().forEach(o -> {
        o.value().init(o);
      });
    }
    if (event.getRegistryKey() == RootsRegistries.Keys.SPELLS) {
      event.getRegistries().registryOrThrow(RootsRegistries.Keys.SPELLS).holders().forEach(o -> {
        o.value().init(o);
      });
    }
    if (event.getRegistryKey() == RootsRegistries.Keys.SPELL_MODIFIERS) {
      event.getRegistries().registryOrThrow(RootsRegistries.Keys.SPELL_MODIFIERS).holders().forEach(o -> {
        o.value().init(o);
      });
    }
  }
}
