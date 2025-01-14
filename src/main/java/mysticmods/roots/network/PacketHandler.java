package mysticmods.roots.network;

import mysticmods.roots.network.client.ClientBoundHerbSyncPacket;
import mysticmods.roots.network.client.ClientBoundReputationSyncPacket;
import mysticmods.roots.network.client.ClientBoundSnapshotSyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class PacketHandler {
  public static String VERSION = "4.0.0.0";

  public PacketHandler(IEventBus modEventBus) {
    modEventBus.addListener(RegisterPayloadHandlersEvent.class,
        event -> {
          PayloadRegistrar registrar = event.registrar(VERSION);
          registerClientToServer(new PacketRegistrar(registrar, true));
          registerServerToClient(new PacketRegistrar(registrar, false));
        });
  }

  protected void registerClientToServer(PacketRegistrar registrar) {
  }

  protected void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(ClientBoundSnapshotSyncPacket.TYPE, ClientBoundSnapshotSyncPacket.CODEC);
    registrar.play(ClientBoundReputationSyncPacket.TYPE, ClientBoundReputationSyncPacket.CODEC);
    registrar.play(ClientBoundHerbSyncPacket.TYPE, ClientBoundHerbSyncPacket.CODEC);
  }

  protected record PacketRegistrar(PayloadRegistrar registrar, boolean toServer) {

    public <MSG extends IRootsPacket> void play(CustomPacketPayload.Type<MSG> type, StreamCodec<? super RegistryFriendlyByteBuf, MSG> reader) {
      if (toServer) {
        registrar.playToServer(type, reader, IRootsPacket::handle);
      } else {
        registrar.playToClient(type, reader, IRootsPacket::handle);
      }
    }
  }
}
