package epicsquid.roots.network;

import epicsquid.roots.network.fx.*;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

  public static void registerMessages() {

    //Spell Effects
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageSanctuaryRingFX.MessageHolder.class, MessageSanctuaryRingFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageSanctuaryBurstFX.MessageHolder.class, MessageSanctuaryBurstFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageDandelionCastFX.MessageHolder.class, MessageDandelionCastFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageShatterBurstFX.MessageHolder.class, MessageShatterBurstFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessagePetalShellBurstFX.MessageHolder.class, MessagePetalShellBurstFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageTimeStopStartFX.MessageHolder.class, MessageTimeStopStartFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLifeDrainAbsorbFX.MessageHolder.class, MessageLifeDrainAbsorbFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageAcidCloudFX.MessageHolder.class, MessageAcidCloudFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLifeInfusionFX.MessageHolder.class, MessageLifeInfusionFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRadianceBeamFX.MessageHolder.class, MessageRadianceBeamFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLightDrifterFX.MessageHolder.class, MessageLightDrifterFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLightDrifterSync.MessageHolder.class, MessageLightDrifterSync.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGeasRingFX.MessageHolder.class, MessageGeasRingFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGeasFX.MessageHolder.class, MessageGeasFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRampantLifeInfusionFX.MessageHolder.class, MessageRampantLifeInfusionFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageOvergrowthEffectFX.MessageHolder.class, MessageOvergrowthEffectFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageTreeCompleteFX.MessageHolder.class, MessageTreeCompleteFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageFrostTouchFX.MessageHolder.class, MessageFrostTouchFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageIcedTouchFX.Handler.class, MessageIcedTouchFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageItemGatheredFX.MessageHolder.class, MessageItemGatheredFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRunicShearsAOEFX.MessageHolder.class, MessageRunicShearsAOEFX.class, Dist.CLIENT);

    //Other Effects
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageImbueCompleteFX.MessageHolder.class, MessageImbueCompleteFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(ElementalSoilTransformFX.Handler.class, ElementalSoilTransformFX.class, Dist.CLIENT);

    //Miscellaneous
    //Client 2 Server
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerUpdateStaff.MessageHolder.class, MessageServerUpdateStaff.class, Side.SERVER);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerTryPickupArrows.MessageHolder.class, MessageServerTryPickupArrows.class, Side.SERVER);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerOpenPouch.MessageHolder.class, MessageServerOpenPouch.class, Side.SERVER);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerOpenQuiver.MessageHolder.class, MessageServerOpenQuiver.class, Side.SERVER);

    //Server 2 Client
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessagePlayerGroveUpdate.MessageHolder.class, MessagePlayerGroveUpdate.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessagePlayerDataUpdate.MessageHolder.class, MessagePlayerDataUpdate.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageClearToasts.MessageHolder.class, MessageClearToasts.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageUpdateHerb.MessageHolder.class, MessageUpdateHerb.class, Dist.CLIENT);

    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGroveCompleteFX.MessageHolder.class, MessageGroveCompleteFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGroveTickFX.MessageHolder.class, MessageGroveTickFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGrowthCrafterVisualFX.MessageHolder.class, MessageGrowthCrafterVisualFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRunicShearsFX.MessageHolder.class, MessageRunicShearsFX.class, Dist.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageFrosLandsProgressFX.MessageHolder.class, MessageFrosLandsProgressFX.class, Dist.CLIENT);
  }
}
