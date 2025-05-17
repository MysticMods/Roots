package mysticmods.roots.network;

import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.network.client.*;
import mysticmods.roots.network.client.fx.*;
import mysticmods.roots.network.server.ServerboundOpenPouchPacket;
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
    registrar.play(ServerboundOpenPouchPacket.TYPE, ServerboundOpenPouchPacket.CODEC);
  }

  protected void registerServerToClient(PacketRegistrar registrar) {
    registrar.play(ClientboundReputationSyncPacket.TYPE, ClientboundReputationSyncPacket.CODEC);
    registrar.play(ClientboundHerbSyncPacket.TYPE, ClientboundHerbSyncPacket.CODEC);
    registrar.play(ClientboundGrantSyncPacket.TYPE, ClientboundGrantSyncPacket.CODEC);
    registrar.play(ClientboundOpenLibraryPacket.TYPE, ClientboundOpenLibraryPacket.CODEC);
    registrar.play(ClientboundEntitySnapshotSyncPacket.TYPE, ClientboundEntitySnapshotSyncPacket.CODEC);
    registrar.play(ClientboundDiscardEntityAttachmentPacket.TYPE, ClientboundDiscardEntityAttachmentPacket.CODEC);
    registrar.play(GrowthFXPacket.TYPE, GrowthFXPacket.CODEC);
    registrar.play(CastChannelTargetFXPacket.TYPE, CastChannelTargetFXPacket.CODEC);
    registrar.play(ClientboundSyncGeasPacket.TYPE, ClientboundSyncGeasPacket.CODEC);
    registrar.play(AlertnessFXPacket.TYPE, AlertnessFXPacket.CODEC);
    registrar.play(ClientboundHerbCountSyncPacket.TYPE, ClientboundHerbCountSyncPacket.CODEC);
    registrar.play(RampantGrowthFXPacket.TYPE, RampantGrowthFXPacket.CODEC);
    registrar.play(ClientboundReputationMessagePacket.TYPE, ClientboundReputationMessagePacket.CODEC);
    registrar.play(AnimalHarvestFXPacket.TYPE, AnimalHarvestFXPacket.CODEC);
    registrar.play(SpiralFXPacket.TYPE, SpiralFXPacket.CODEC);
    registrar.play(CastChannelFXPacket.TYPE, CastChannelFXPacket.CODEC);
    registrar.play(CastChannelFailFXPacket.TYPE, CastChannelFailFXPacket.CODEC);
    registrar.play(AquaBubbleFXPacket.TYPE, AquaBubbleFXPacket.CODEC);
    registrar.play(CastAquaBubbleFXPacket.TYPE, CastAquaBubbleFXPacket.CODEC);
    registrar.play(DisarmFXPacket.TYPE, DisarmFXPacket.CODEC);
    registrar.play(LightningFXPacket.TYPE, LightningFXPacket.CODEC);
    registrar.play(AcidCloudFXPacket.TYPE, AcidCloudFXPacket.CODEC);
    registrar.play(DandelionWindsFXPacket.TYPE, DandelionWindsFXPacket.CODEC);
    registrar.play(CastSkySoarerFXPacket.TYPE, CastSkySoarerFXPacket.CODEC);
    registrar.play(CastExtensionFXPacket.TYPE, CastExtensionFXPacket.CODEC);
    registrar.play(CastShatterFX.TYPE, CastShatterFX.CODEC);
    registrar.play(CastMagnetismFXPacket.TYPE, CastMagnetismFXPacket.CODEC);
    registrar.play(PetalShellFXPacket.TYPE, PetalShellFXPacket.CODEC);
    registrar.play(ClientboundOpenReputationPacket.TYPE, ClientboundOpenReputationPacket.CODEC);
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
