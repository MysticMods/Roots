package mysticmods.roots.init;

import com.google.common.eventbus.Subscribe;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.registry.RootsRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import net.neoforged.neoforge.registries.NewRegistryEvent;

@EventBusSubscriber(modid = RootsAPI.MODID)
public class ModRegistries {
  @SubscribeEvent
  public static void onNewRegistries(NewRegistryEvent event) {
    event.register(RootsRegistries.HERBS);
    event.register(RootsRegistries.RITUALS);
    event.register(RootsRegistries.SPELLS);
    event.register(RootsRegistries.SPELL_MODIFIERS);
    event.register(RootsRegistries.PROPERTY_SERIALIZERS);
    event.register(RootsRegistries.PROPERTY_TYPES);
    event.register(RootsRegistries.CONDITION_TYPES);
    event.register(RootsRegistries.ENTITY_TEST_TYPES);
    event.register(RootsRegistries.WORLD_TEST_TYPES);
    event.register(RootsRegistries.SNAPSHOT_TYPES);
    event.register(RootsRegistries.GROVES);
    event.register(RootsRegistries.CAN_GROW_FUNCTIONS);
    event.register(RootsRegistries.HARVEST_FUNCTIONS);
    event.register(RootsRegistries.LIGHT_FUNCTIONS);
    event.register(RootsRegistries.CAN_HARVEST_FUNCTIONS);
    event.register(RootsRegistries.GROVE_ACTIONS);
    event.register(RootsRegistries.RITUAL_MODIFIERS);
  }

  @SubscribeEvent
  public static void onDataPackRegistries (DataPackRegistryEvent.NewRegistry event) {
    event.dataPackRegistry(RootsRegistries.Keys.CONDITIONS, ICondition.DIRECT_CODEC, ICondition.DIRECT_CODEC);
  }
}

