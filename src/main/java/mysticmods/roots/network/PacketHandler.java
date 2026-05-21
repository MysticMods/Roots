package mysticmods.roots.network;

import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.network.client.*;
import mysticmods.roots.network.client.fx.*;
import mysticmods.roots.network.client.fx.lightning.DynamicLightningFXPacket;
import mysticmods.roots.network.client.fx.lightning.SemiDynamicLightningFXPacket;
import mysticmods.roots.network.client.fx.lightning.StaticLightningFXPacket;
import mysticmods.roots.network.server.*;
import mysticmods.roots.network.server.debug.ServerboundDebugScreenTick;
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
    registrar.play(ServerboundCycleTomePacket.TYPE, ServerboundCycleTomePacket.CODEC);
    registrar.play(ServerboundClearStaffSlotPacket.TYPE, ServerboundClearStaffSlotPacket.CODEC);
    registrar.play(ServerboundMoveLightDrifterPacket.Pos.TYPE, ServerboundMoveLightDrifterPacket.Pos.CODEC);
    registrar.play(ServerboundMoveLightDrifterPacket.PosRot.TYPE, ServerboundMoveLightDrifterPacket.PosRot.CODEC);
    registrar.play(ServerboundMoveLightDrifterPacket.Rot.TYPE, ServerboundMoveLightDrifterPacket.Rot.CODEC);
    registrar.play(ServerboundCancelLightDrifterPacket.TYPE, ServerboundCancelLightDrifterPacket.CODEC);
    registrar.play(ServerboundCancelEffectPacket.TYPE, ServerboundCancelEffectPacket.CODEC);
    registrar.play(ServerboundFakeMenuPacket.TYPE, ServerboundFakeMenuPacket.CODEC);
    registrar.play(ServerboundClearContainerPacket.TYPE, ServerboundClearContainerPacket.CODEC);
    registrar.play(ServerboundToggleSpellModifierPacket.TYPE, ServerboundToggleSpellModifierPacket.CODEC);
    registrar.play(ServerboundDebugScreenTick.TYPE, ServerboundDebugScreenTick.CODEC);
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
    registrar.play(StaticLightningFXPacket.TYPE, StaticLightningFXPacket.CODEC);
    registrar.play(DynamicLightningFXPacket.TYPE, DynamicLightningFXPacket.CODEC);
    registrar.play(SemiDynamicLightningFXPacket.TYPE, SemiDynamicLightningFXPacket.CODEC);
    registrar.play(AcidCloudFXPacket.TYPE, AcidCloudFXPacket.CODEC);
    registrar.play(DandelionWindsFXPacket.TYPE, DandelionWindsFXPacket.CODEC);
    registrar.play(CastSkySoarerFXPacket.TYPE, CastSkySoarerFXPacket.CODEC);
    registrar.play(CastExtensionFXPacket.TYPE, CastExtensionFXPacket.CODEC);
    registrar.play(CastShatterFX.TYPE, CastShatterFX.CODEC);
    registrar.play(CastMagnetismFXPacket.TYPE, CastMagnetismFXPacket.CODEC);
    registrar.play(PetalShellFXPacket.TYPE, PetalShellFXPacket.CODEC);
    registrar.play(CastChannelJauntFXPacket.TYPE, CastChannelJauntFXPacket.CODEC);
    registrar.play(EntityBeamFXPacket.TYPE, EntityBeamFXPacket.CODEC);
    registrar.play(ClientboundOpenReputationPacket.TYPE, ClientboundOpenReputationPacket.CODEC);
    registrar.play(CastLifeDrainFXPacket.TYPE, CastLifeDrainFXPacket.CODEC);
    registrar.play(DrainLifeFXPacket.TYPE, DrainLifeFXPacket.CODEC);
    registrar.play(HarvestFXPacket.TYPE, HarvestFXPacket.CODEC);
    registrar.play(DesaturateScreenFXPacket.TYPE, DesaturateScreenFXPacket.CODEC);
    registrar.play(HealFXPacket.TYPE, HealFXPacket.CODEC);
    registrar.play(NondetectionFXPacket.TYPE, NondetectionFXPacket.CODEC);
    registrar.play(SanctuaryFXPacket.TYPE, SanctuaryFXPacket.CODEC);
    registrar.play(SaturateScreenFXPacket.TYPE, SaturateScreenFXPacket.CODEC);
    registrar.play(StartGroveCraftingFX.TYPE, StartGroveCraftingFX.CODEC);
    registrar.play(GrowthAmplifierFXPacket.TYPE, GrowthAmplifierFXPacket.CODEC);
    registrar.play(ClientboundCooldownSyncPacket.TYPE, ClientboundCooldownSyncPacket.CODEC);
    registrar.play(ClientboundAnimalHarvestSyncPacket.TYPE, ClientboundAnimalHarvestSyncPacket.CODEC);
    registrar.play(ClientboundClearHighlightPacket.TYPE, ClientboundClearHighlightPacket.CODEC);
    registrar.play(ClientboundLightDrifterSyncPacket.TYPE, ClientboundLightDrifterSyncPacket.CODEC);
    registrar.play(ClientboundStopPlayerMovementPacket.TYPE, ClientboundStopPlayerMovementPacket.CODEC);
    registrar.play(ClientboundChangeTomeMode.TYPE, ClientboundChangeTomeMode.CODEC);
    registrar.play(ClientboundPouchPickUpHerbPacket.TYPE, ClientboundPouchPickUpHerbPacket.CODEC);
    registrar.play(ClientboundRefreshModifierScreenPacket.TYPE, ClientboundRefreshModifierScreenPacket.CODEC);
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
