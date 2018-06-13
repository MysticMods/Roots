package teamroots.roots.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import teamroots.roots.Roots;
import teamroots.roots.network.message.*;

public class PacketHandler {
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Roots.MODID);

    private static int id = 0;
    
    public static void registerMessages(){
        INSTANCE.registerMessage(MessageMoonlightBurstFX.MessageHolder.class,MessageMoonlightBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageTEUpdate.MessageHolder.class,MessageTEUpdate.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageImbueCompleteFX.MessageHolder.class,MessageImbueCompleteFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageSanctuaryRingFX.MessageHolder.class,MessageSanctuaryRingFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageSanctuaryBurstFX.MessageHolder.class,MessageSanctuaryBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageDandelionCastFX.MessageHolder.class,MessageDandelionCastFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageShatterBurstFX.MessageHolder.class,MessageShatterBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessagePetalShellBurstFX.MessageHolder.class,MessagePetalShellBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageTimeStopStartFX.MessageHolder.class,MessageTimeStopStartFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageLifeDrainAbsorbFX.MessageHolder.class,MessageLifeDrainAbsorbFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageAcidCloudFX.MessageHolder.class,MessageAcidCloudFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageLifeInfusionFX.MessageHolder.class,MessageLifeInfusionFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageMindWardFX.MessageHolder.class,MessageMindWardFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageMindWardRingFX.MessageHolder.class,MessageMindWardRingFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageRadianceBeamFX.MessageHolder.class,MessageRadianceBeamFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageLightDrifterFX.MessageHolder.class,MessageLightDrifterFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageLightDrifterSync.MessageHolder.class,MessageLightDrifterSync.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageTrapPaperFX.MessageHolder.class,MessageTrapPaperFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageLecternAddFX.MessageHolder.class,MessageLecternAddFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageLecternCompleteFX.MessageHolder.class,MessageLecternCompleteFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageMoonlightSparkleFX.MessageHolder.class,MessageMoonlightSparkleFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessagePlayerDataUpdate.MessageHolder.class,MessagePlayerDataUpdate.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageFairyDustBurstFX.MessageHolder.class,MessageFairyDustBurstFX.class,id ++,Side.CLIENT);
        INSTANCE.registerMessage(MessageFairyTameFX.MessageHolder.class,MessageFairyTameFX.class,id ++,Side.CLIENT);
    }
}
