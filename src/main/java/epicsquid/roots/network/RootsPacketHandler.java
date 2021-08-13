package epicsquid.roots.network;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.network.fx.*;
import net.minecraftforge.fml.relauncher.Side;

public class RootsPacketHandler {

  public static void registerMessages() {

    //Spell Effects
    PacketHandler.registerMessage(MessageSanctuaryRingFX.MessageHolder.class, MessageSanctuaryRingFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageSanctuaryBurstFX.MessageHolder.class, MessageSanctuaryBurstFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageDandelionCastFX.MessageHolder.class, MessageDandelionCastFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageShatterBurstFX.MessageHolder.class, MessageShatterBurstFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessagePetalShellBurstFX.MessageHolder.class, MessagePetalShellBurstFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageTimeStopStartFX.MessageHolder.class, MessageTimeStopStartFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageLifeDrainAbsorbFX.MessageHolder.class, MessageLifeDrainAbsorbFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageAcidCloudFX.MessageHolder.class, MessageAcidCloudFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageLifeInfusionFX.MessageHolder.class, MessageLifeInfusionFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRadianceBeamFX.MessageHolder.class, MessageRadianceBeamFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageLightDrifterFX.MessageHolder.class, MessageLightDrifterFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageLightDrifterSync.MessageHolder.class, MessageLightDrifterSync.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageGeasRingFX.MessageHolder.class, MessageGeasRingFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageGeasFX.MessageHolder.class, MessageGeasFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageUnusedPlantFX.Handler.class, MessageUnusedPlantFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRampantLifeInfusionFX.MessageHolder.class, MessageRampantLifeInfusionFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageOvergrowthEffectFX.MessageHolder.class, MessageOvergrowthEffectFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageTreeCompleteFX.MessageHolder.class, MessageTreeCompleteFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageStormCloudGasFX.MessageHolder.class, MessageStormCloudGasFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageWinterCloudFX.Handler.class, MessageWinterCloudFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageSenseFX.Handler.class, MessageSenseFX.class, Side.CLIENT);

    //Other Effects
    PacketHandler.registerMessage(MessageImbueCompleteFX.MessageHolder.class, MessageImbueCompleteFX.class, Side.CLIENT);
    PacketHandler.registerMessage(ElementalSoilTransformFX.Handler.class, ElementalSoilTransformFX.class, Side.CLIENT);

    //Miscellaneous
    //Client 2 Server
    PacketHandler.registerMessage(MessageServerTryPickupArrows.MessageHolder.class, MessageServerTryPickupArrows.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerOpenPouch.MessageHolder.class, MessageServerOpenPouch.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerOpenQuiver.MessageHolder.class, MessageServerOpenQuiver.class, Side.SERVER);
    PacketHandler.registerMessage(MessageSetImposerSlot.MessageHolder.class, MessageSetImposerSlot.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerOpenLibrary.MessageHolder.class, MessageServerOpenLibrary.class, Side.SERVER);
    PacketHandler.registerMessage(MessageResetLibraryScreen.MessageHolder.class, MessageResetLibraryScreen.class, Side.SERVER);

    //Server 2 Client
    PacketHandler.registerMessage(MessageDisarmFX.Handler.class, MessageDisarmFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageFallBladesFX.Handler.class, MessageFallBladesFX.class, Side.CLIENT);

    PacketHandler.registerMessage(MessageClearToasts.MessageHolder.class, MessageClearToasts.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageUpdateHerb.MessageHolder.class, MessageUpdateHerb.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageInvalidateContainer.MessageHolder.class, MessageInvalidateContainer.class, Side.CLIENT);

    PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageGroveCompleteFX.MessageHolder.class, MessageGroveCompleteFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageGroveTickFX.MessageHolder.class, MessageGroveTickFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageGrowthCrafterVisualFX.MessageHolder.class, MessageGrowthCrafterVisualFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRunicShearsFX.MessageHolder.class, MessageRunicShearsFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRunicShearsBlockFX.MessageHolder.class, MessageRunicShearsBlockFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageFrostLandsProgressFX.MessageHolder.class, MessageFrostLandsProgressFX.class, Side.CLIENT);

    PacketHandler.registerMessage(MessageItemGatheredFX.MessageHolder.class, MessageItemGatheredFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRunicShearsAOEFX.MessageHolder.class, MessageRunicShearsAOEFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageChrysopoeiaFX.Handler.class, MessageChrysopoeiaFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageSaturationFX.Handler.class, MessageSaturationFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageDesaturationFX.Handler.class, MessageDesaturationFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessagePatchouliJEI.MessageHolder.class, MessagePatchouliJEI.class, Side.CLIENT);
/*    PacketHandler.registerMessage(MessageThawFX.MessageHolder.class, MessageThawFX.class, Side.CLIENT);*/
    PacketHandler.registerMessage(MessageDisarmFX.Handler.class, MessageDisarmFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageSenseHomeFX.MessageHolder.class, MessageSenseHomeFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageTargetedGeasFX.MessageHolder.class, MessageTargetedGeasFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageTargetedLifeDrainFX.MessageHolder.class, MessageTargetedLifeDrainFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageChemTrailsFX.Handler.class, MessageChemTrailsFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageStormCloudStormFX.MessageHolder.class, MessageStormCloudStormFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRoseThornsTickFX.MessageHolder.class, MessageRoseThornsTickFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageRoseThornsBurstFX.MessageHolder.class, MessageRoseThornsBurstFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessagePetalShellRingFX.MessageHolder.class, MessagePetalShellRingFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessagePyreBigFlameFX.Handler.class, MessagePyreBigFlameFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageAquaBubbleFX.MessageHolder.class, MessageAquaBubbleFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageServerModifier.MessageHolder.class, MessageServerModifier.class, Side.SERVER);
    PacketHandler.registerMessage(MessageCreatureSummonedFX.MessageHolder.class, MessageCreatureSummonedFX.class, Side.CLIENT);
    PacketHandler.registerMessage(MessageTradeResetFX.Handler.class, MessageTradeResetFX.class, Side.CLIENT);
  }
}
