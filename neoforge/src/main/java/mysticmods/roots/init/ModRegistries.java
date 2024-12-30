package mysticmods.roots.init;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid=RootsAPI.MODID, bus=EventBusSubscriber.Bus.MOD)
public class ModRegistries {
  @SubscribeEvent
  public static void onNewRegistries (NewRegistryEvent event) {
    event.register(RootsRegistries.HERBS);
    event.register(RootsRegistries.RITUALS);
    event.register(RootsRegistries.SPELLS);
    event.register(RootsRegistries.SPELL_MODIFIERS);
    event.register(RootsRegistries.RITUAL_PROPERTIES);
    event.register(RootsRegistries.SPELL_PROPERTIES);
    event.register(RootsRegistries.LEVEL_CONDITIONS);
    event.register(RootsRegistries.PLAYER_CONDITIONS);
    event.register(RootsRegistries.SNAPSHOT_SERIALIZERS);
    event.register(RootsRegistries.ENTITY_TEST_TYPES);
    event.register(RootsRegistries.GROVES);
  }
}

