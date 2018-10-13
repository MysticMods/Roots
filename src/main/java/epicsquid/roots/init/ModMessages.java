package epicsquid.roots.init;

import epicsquid.mysticallib.network.PacketHandler;
import epicsquid.roots.network.message.MessagePlayerGroveUpdate;
import epicsquid.roots.network.message.fx.MessageAcidCloudFX;
import epicsquid.roots.network.message.fx.MessageDandelionCastFX;
import epicsquid.roots.network.message.fx.MessageImbueCompleteFX;
import epicsquid.roots.network.message.fx.MessageLifeDrainAbsorbFX;
import epicsquid.roots.network.message.fx.MessageLifeInfusionFX;
import epicsquid.roots.network.message.fx.MessageLightDrifterFX;
import epicsquid.roots.network.message.fx.MessageLightDrifterSync;
import epicsquid.roots.network.message.fx.MessagePetalShellBurstFX;
import epicsquid.roots.network.message.fx.MessageRadianceBeamFX;
import epicsquid.roots.network.message.fx.MessageSanctuaryBurstFX;
import epicsquid.roots.network.message.fx.MessageSanctuaryRingFX;
import epicsquid.roots.network.message.fx.MessageShatterBurstFX;
import epicsquid.roots.network.message.fx.MessageTimeStopStartFX;
import net.minecraftforge.fml.relauncher.Side;

public class ModMessages {

  public static void registerMessages() {
    PacketHandler.registerMessage(MessageImbueCompleteFX.MessageHolder.class, MessageImbueCompleteFX.class, Side.CLIENT);
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
    PacketHandler.registerMessage(MessagePlayerGroveUpdate.MessageHolder.class, MessagePlayerGroveUpdate.class, Side.CLIENT);
  }
}
