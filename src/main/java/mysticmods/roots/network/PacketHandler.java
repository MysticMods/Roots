package mysticmods.roots.network;

import mysticmods.roots.network.client.*;
import mysticmods.roots.network.server.ServerboundSetSpellDataPacket;
import mysticmods.roots.network.server.ServerboundSetSpellPacket;
import mysticmods.roots.network.server.ServerboundSwapSpellsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


public class PacketHandler {
  public static String VERSION = "4.0.0.0";

  public PacketHandler(IEventBus modEventBus) {
    modEventBus.addListener(RegisterPayloadHandlersEvent.class,
        event -> {
          PayloadRegistrar registrar = event.registrar(VERSION).executesOn(HandlerThread.MAIN);
          registerClientToServer(new PacketRegistrar(registrar, true));
          registerServerToClient(new PacketRegistrar(registrar, false));
        });
  }

  protected void registerClientToServer(PacketRegistrar registrar) {
    registrar.play(ServerboundSetSpellPacket.TYPE, ServerboundSetSpellPacket.CODEC);
    registrar.play(ServerboundSwapSpellsPacket.TYPE, ServerboundSwapSpellsPacket.CODEC);
    registrar.play(ServerboundSetSpellDataPacket.TYPE, ServerboundSetSpellDataPacket.CODEC);
  }

  protected void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(ClientboundPlayerSnapshotSyncPacket.TYPE, ClientboundPlayerSnapshotSyncPacket.CODEC);
    registrar.play(ClientboundReputationSyncPacket.TYPE, ClientboundReputationSyncPacket.CODEC);
    registrar.play(ClientboundHerbSyncPacket.TYPE, ClientboundHerbSyncPacket.CODEC);
    registrar.play(ClientboundGrantSyncPacket.TYPE, ClientboundGrantSyncPacket.CODEC);
    registrar.play(ClientboundOpenLibraryPacket.TYPE, ClientboundOpenLibraryPacket.CODEC);
    registrar.play(ClientboundEntitySnapshotSyncPacket.TYPE, ClientboundEntitySnapshotSyncPacket.CODEC);
    registrar.play(ClientboundDiscardEntityAttachmentPacket.TYPE, ClientboundDiscardEntityAttachmentPacket.CODEC);
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
