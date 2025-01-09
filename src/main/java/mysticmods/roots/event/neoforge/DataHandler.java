package mysticmods.roots.event.neoforge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.data.listener.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;


@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataHandler {
  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(new InitializeReloadListener());
    // TODO: Add resets for various things???
  }
  public static void init () {
    RootsRegistries.RITUALS.stream().forEach(Ritual::init);
    RootsRegistries.SPELLS.stream().forEach(Spell::init);
  }

  @SubscribeEvent
  public static void onDataReloaded(OnDatapackSyncEvent event) {
    init();
    if (event.getPlayer() != null) {
    } else {
    }
  }

}
