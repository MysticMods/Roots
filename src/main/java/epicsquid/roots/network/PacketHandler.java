package epicsquid.roots.network;

import epicsquid.roots.network.fx.*;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

  public static void registerMessages() {

    //Spell Effects
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageSanctuaryRingFX.MessageHolder.class, MessageSanctuaryRingFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageSanctuaryBurstFX.MessageHolder.class, MessageSanctuaryBurstFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageDandelionCastFX.MessageHolder.class, MessageDandelionCastFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageShatterBurstFX.MessageHolder.class, MessageShatterBurstFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessagePetalShellBurstFX.MessageHolder.class, MessagePetalShellBurstFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageTimeStopStartFX.MessageHolder.class, MessageTimeStopStartFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLifeDrainAbsorbFX.MessageHolder.class, MessageLifeDrainAbsorbFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageAcidCloudFX.MessageHolder.class, MessageAcidCloudFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLifeInfusionFX.MessageHolder.class, MessageLifeInfusionFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRadianceBeamFX.MessageHolder.class, MessageRadianceBeamFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLightDrifterFX.MessageHolder.class, MessageLightDrifterFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageLightDrifterSync.MessageHolder.class, MessageLightDrifterSync.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGeasRingFX.MessageHolder.class, MessageGeasRingFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGeasFX.MessageHolder.class, MessageGeasFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRampantLifeInfusionFX.MessageHolder.class, MessageRampantLifeInfusionFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageOvergrowthEffectFX.MessageHolder.class, MessageOvergrowthEffectFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageTreeCompleteFX.MessageHolder.class, MessageTreeCompleteFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageFrostTouchFX.MessageHolder.class, MessageFrostTouchFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageIcedTouchFX.Handler.class, MessageIcedTouchFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageItemGatheredFX.MessageHolder.class, MessageItemGatheredFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRunicShearsAOEFX.MessageHolder.class, MessageRunicShearsAOEFX.class, Side.CLIENT);

    //Other Effects
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageImbueCompleteFX.MessageHolder.class, MessageImbueCompleteFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(ElementalSoilTransformFX.Handler.class, ElementalSoilTransformFX.class, Side.CLIENT);

    //Miscellaneous
    //Client 2 Server
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerUpdateStaff.MessageHolder.class, MessageServerUpdateStaff.class, Side.SERVER);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerTryPickupArrows.MessageHolder.class, MessageServerTryPickupArrows.class, Side.SERVER);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerOpenPouch.MessageHolder.class, MessageServerOpenPouch.class, Side.SERVER);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageServerOpenQuiver.MessageHolder.class, MessageServerOpenQuiver.class, Side.SERVER);

    //Server 2 Client
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageClearToasts.MessageHolder.class, MessageClearToasts.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageUpdateHerb.MessageHolder.class, MessageUpdateHerb.class, Side.CLIENT);

    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGroveCompleteFX.MessageHolder.class, MessageGroveCompleteFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGroveTickFX.MessageHolder.class, MessageGroveTickFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageGrowthCrafterVisualFX.MessageHolder.class, MessageGrowthCrafterVisualFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageRunicShearsFX.MessageHolder.class, MessageRunicShearsFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageFrosLandsProgressFX.MessageHolder.class, MessageFrosLandsProgressFX.class, Side.CLIENT);
  }
}
