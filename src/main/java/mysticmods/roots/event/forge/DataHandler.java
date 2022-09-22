package mysticmods.roots.event.forge;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.gen.listener.ModifierCostReloadListener;
import mysticmods.roots.gen.listener.RitualPropertyReloadListener;
import mysticmods.roots.gen.listener.SpellCostReloadListener;
import mysticmods.roots.gen.listener.SpellPropertyReloadListener;
import mysticmods.roots.network.Networking;
import mysticmods.roots.network.client.*;
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
    } else {
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundRitualPropertyPacket());
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundSpellPropertyPacket());
      Networking.send(PacketDistributor.ALL.noArg(), new ClientBoundSpellCostsPacket());
    }
  }

}
