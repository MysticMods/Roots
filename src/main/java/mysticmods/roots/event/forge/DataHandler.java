package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.gen.listener.ModifierCostReloadListener;
import mysticmods.roots.gen.listener.RitualPropertyReloadListener;
import mysticmods.roots.gen.listener.SpellCostReloadListener;
import mysticmods.roots.gen.listener.SpellPropertyReloadListener;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.ClientBoundRitualPropertyPacket;
import mysticmods.roots.network.client.ClientBoundSpellCostsPacket;
import mysticmods.roots.network.client.ClientBoundSpellPropertyPacket;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = RootsAPI.MODID)
public class DataHandler {
  @SubscribeEvent
  public static void onReloadListeners(AddReloadListenerEvent event) {
    event.addListener(RitualPropertyReloadListener.getInstance());
    event.addListener(SpellPropertyReloadListener.getInstance());
    event.addListener(SpellCostReloadListener.getInstance());
    event.addListener(ModifierCostReloadListener.getInstance());
  }

  @SubscribeEvent
  public static void onDataReloaded(OnDatapackSyncEvent event) {
    if (event.getPlayer() != null) {
      Networking.sendTo(new ClientBoundRitualPropertyPacket(), event.getPlayer());
      Networking.sendTo(new ClientBoundSpellPropertyPacket(), event.getPlayer());
      Networking.sendTo(new ClientBoundSpellCostsPacket(), event.getPlayer());
      // TODO: DO NOT DO THIS HERE
      //Networking.sendTo(new ClientBoundCapabilitySynchronization(event.getPlayer(), RootsAPI.GRANT_CAPABILITY_ID), event.getPlayer());
      //Networking.sendTo(new ClientBoundCapabilitySynchronization(event.getPlayer(), RootsAPI.HERB_CAPABILITY_ID), event.getPlayer());
    } else {
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundRitualPropertyPacket());
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundSpellPropertyPacket());
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundSpellCostsPacket());
      // TODO: Capability syncs probably shouldn't be here initially
/*      for (ServerPlayer player : event.getPlayerList().getPlayers()) {
        Networking.sendTo(new ClientBoundCapabilitySynchronization(player, RootsAPI.GRANT_CAPABILITY_ID), player);
        Networking.sendTo(new ClientBoundCapabilitySynchronization(player, RootsAPI.HERB_CAPABILITY_ID), player);
      }*/
    }
  }
}
