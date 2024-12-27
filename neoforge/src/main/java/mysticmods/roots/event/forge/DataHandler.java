package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.ritual.Ritual;
import mysticmods.roots.api.spell.Spell;
import mysticmods.roots.data.listener.*;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundRitualPropertyPacket;
import mysticmods.roots.network.client.ClientBoundSpellCostsPacket;
import mysticmods.roots.network.client.ClientBoundSpellPropertyPacket;






@EventBusSubscriber(modid = RootsAPI.MODID)
public class DataHandler {
  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(RitualPropertyReloadListener.getInstance());
    event.addListener(SpellPropertyReloadListener.getInstance());
    event.addListener(SpellCostReloadListener.getInstance());
    event.addListener(ModifierCostReloadListener.getInstance());
    event.addListener(new InitializeReloadListener());
    // TODO: Add resets for various things???
  }
  public static void init () {
    for (Ritual ritual : RootsRegistries.RITUAL_REGISTRY.get().getValues()) {
      ritual.init();
    }
    for (Spell spell : RootsRegistries.SPELL_REGISTRY.get().getValues()) {
      spell.init();
    }
  }

  @SubscribeEvent
  public static void onDataReloaded(OnDatapackSyncEvent event) {
    init();
    if (event.getPlayer() != null) {
      Networking.sendTo(new ClientBoundRitualPropertyPacket(), event.getPlayer());
      Networking.sendTo(new ClientBoundSpellPropertyPacket(), event.getPlayer());
      Networking.sendTo(new ClientBoundSpellCostsPacket(), event.getPlayer());
    } else {
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundRitualPropertyPacket());
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundSpellPropertyPacket());
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundSpellCostsPacket());
    }
  }

}
