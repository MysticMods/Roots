package epicsquid.roots.network;

import epicsquid.roots.Roots;
import epicsquid.roots.network.message.MessageAcidCloudFX;
import epicsquid.roots.network.message.MessageDandelionCastFX;
import epicsquid.roots.network.message.MessageImbueCompleteFX;
import epicsquid.roots.network.message.MessageLifeDrainAbsorbFX;
import epicsquid.roots.network.message.MessageLifeInfusionFX;
import epicsquid.roots.network.message.MessageLightDrifterFX;
import epicsquid.roots.network.message.MessageLightDrifterSync;
import epicsquid.roots.network.message.MessagePetalShellBurstFX;
import epicsquid.roots.network.message.MessageRadianceBeamFX;
import epicsquid.roots.network.message.MessageSanctuaryBurstFX;
import epicsquid.roots.network.message.MessageSanctuaryRingFX;
import epicsquid.roots.network.message.MessageShatterBurstFX;
import epicsquid.roots.network.message.MessageTimeStopStartFX;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
  public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Roots.MODID);

  private static int id = 0;

  public static void registerMessages() {
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
    INSTANCE.registerMessage(MessageRadianceBeamFX.MessageHolder.class,MessageRadianceBeamFX.class,id ++,Side.CLIENT);
    INSTANCE.registerMessage(MessageLightDrifterFX.MessageHolder.class,MessageLightDrifterFX.class,id ++,Side.CLIENT);
    INSTANCE.registerMessage(MessageLightDrifterSync.MessageHolder.class,MessageLightDrifterSync.class,id ++,Side.CLIENT);

  }

  public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handler, Class<REQ> message,
      Side side) {
    INSTANCE.registerMessage(handler, message, id++, side);
  }
}