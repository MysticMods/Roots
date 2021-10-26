package epicsquid.roots.network;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.network.fx.*;
import net.minecraftforge.fml.relauncher.Side;

public class RootsPacketHandler {

  public static void registerMessages() {

    //Spell Effects
    PacketHandler.registerMessage(MessageSanctuaryRingFX.MessageHolder.class, MessageSanctuaryRingFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageSanctuaryBurstFX.MessageHolder.class, MessageSanctuaryBurstFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageDandelionCastFX.MessageHolder.class, MessageDandelionCastFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageShatterBurstFX.MessageHolder.class, MessageShatterBurstFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessagePetalShellBurstFX.MessageHolder.class, MessagePetalShellBurstFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageTimeStopStartFX.MessageHolder.class, MessageTimeStopStartFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageLifeDrainAbsorbFX.MessageHolder.class, MessageLifeDrainAbsorbFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageAcidCloudFX.MessageHolder.class, MessageAcidCloudFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageLifeInfusionFX.MessageHolder.class, MessageLifeInfusionFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRadianceBeamFX.MessageHolder.class, MessageRadianceBeamFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageLightDrifterFX.MessageHolder.class, MessageLightDrifterFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageLightDrifterSync.MessageHolder.class, MessageLightDrifterSync.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageGeasRingFX.MessageHolder.class, MessageGeasRingFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageGeasFX.MessageHolder.class, MessageGeasFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageUnusedPlantFX.Handler.class, MessageUnusedPlantFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRampantLifeInfusionFX.MessageHolder.class, MessageRampantLifeInfusionFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageOvergrowthEffectFX.MessageHolder.class, MessageOvergrowthEffectFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageTreeCompleteFX.MessageHolder.class, MessageTreeCompleteFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageStormCloudGasFX.MessageHolder.class, MessageStormCloudGasFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageWinterCloudFX.Handler.class, MessageWinterCloudFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageSenseFX.Handler.class, MessageSenseFX.class, Dist.CLIENT);

    //Other Effects
    PacketHandler.registerMessage(MessageImbueCompleteFX.MessageHolder.class, MessageImbueCompleteFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(ElementalSoilTransformFX.Handler.class, ElementalSoilTransformFX.class, Dist.CLIENT);

    //Miscellaneous
    //Client 2 Server
    PacketHandler.registerMessage(MessageServerTryPickupArrows.MessageHolder.class, MessageServerTryPickupArrows.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerOpenPouch.MessageHolder.class, MessageServerOpenPouch.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerOpenQuiver.MessageHolder.class, MessageServerOpenQuiver.class, Side.SERVER);
    PacketHandler.registerMessage(MessageSetImposerSlot.MessageHolder.class, MessageSetImposerSlot.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerOpenLibrary.MessageHolder.class, MessageServerOpenLibrary.class, Side.SERVER);
    PacketHandler.registerMessage(MessageResetLibraryScreen.MessageHolder.class, MessageResetLibraryScreen.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerCycleSlot.MessageHolder.class, MessageServerCycleSlot.class, Side.SERVER);
    PacketHandler.registerMessage(MessageServerDeleteSpell.MessageHolder.class, MessageServerDeleteSpell.class, Side.SERVER);

    //Server 2 Client
    PacketHandler.registerMessage(MessageDisarmFX.Handler.class, MessageDisarmFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageFallBladesFX.Handler.class, MessageFallBladesFX.class, Dist.CLIENT);

    PacketHandler.registerMessage(MessageClearToasts.MessageHolder.class, MessageClearToasts.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageUpdateHerb.MessageHolder.class, MessageUpdateHerb.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageInvalidateContainer.MessageHolder.class, MessageInvalidateContainer.class, Dist.CLIENT);

    PacketHandler.registerMessage(MessageHarvestCompleteFX.MessageHolder.class, MessageHarvestCompleteFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageGroveCompleteFX.MessageHolder.class, MessageGroveCompleteFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageGroveTickFX.MessageHolder.class, MessageGroveTickFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageGrowthCrafterVisualFX.MessageHolder.class, MessageGrowthCrafterVisualFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRunicShearsFX.MessageHolder.class, MessageRunicShearsFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRunicShearsBlockFX.MessageHolder.class, MessageRunicShearsBlockFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageFrostLandsProgressFX.MessageHolder.class, MessageFrostLandsProgressFX.class, Dist.CLIENT);

    PacketHandler.registerMessage(MessageItemGatheredFX.MessageHolder.class, MessageItemGatheredFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRunicShearsAOEFX.MessageHolder.class, MessageRunicShearsAOEFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageChrysopoeiaFX.Handler.class, MessageChrysopoeiaFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageSaturationFX.Handler.class, MessageSaturationFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageDesaturationFX.Handler.class, MessageDesaturationFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessagePatchouliJEI.MessageHolder.class, MessagePatchouliJEI.class, Dist.CLIENT);
/*    PacketHandler.registerMessage(MessageThawFX.MessageHolder.class, MessageThawFX.class, Dist.CLIENT);*/
    PacketHandler.registerMessage(MessageDisarmFX.Handler.class, MessageDisarmFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageSenseHomeFX.MessageHolder.class, MessageSenseHomeFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageTargetedGeasFX.MessageHolder.class, MessageTargetedGeasFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageTargetedLifeDrainFX.MessageHolder.class, MessageTargetedLifeDrainFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageChemTrailsFX.Handler.class, MessageChemTrailsFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageStormCloudStormFX.MessageHolder.class, MessageStormCloudStormFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRoseThornsTickFX.MessageHolder.class, MessageRoseThornsTickFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageRoseThornsBurstFX.MessageHolder.class, MessageRoseThornsBurstFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessagePetalShellRingFX.MessageHolder.class, MessagePetalShellRingFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessagePyreBigFlameFX.Handler.class, MessagePyreBigFlameFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageAquaBubbleFX.MessageHolder.class, MessageAquaBubbleFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageServerModifier.MessageHolder.class, MessageServerModifier.class, Side.SERVER);
    PacketHandler.registerMessage(MessageCreatureSummonedFX.MessageHolder.class, MessageCreatureSummonedFX.class, Dist.CLIENT);
    PacketHandler.registerMessage(MessageTradeResetFX.Handler.class, MessageTradeResetFX.class, Dist.CLIENT);
  }
}
