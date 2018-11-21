package epicsquid.roots.network;

import epicsquid.roots.network.fx.MessageAcidCloudFX;
import epicsquid.roots.network.fx.MessageDandelionCastFX;
import epicsquid.roots.network.fx.MessageImbueCompleteFX;
import epicsquid.roots.network.fx.MessageLifeDrainAbsorbFX;
import epicsquid.roots.network.fx.MessageLifeInfusionFX;
import epicsquid.roots.network.fx.MessageLightDrifterFX;
import epicsquid.roots.network.fx.MessageLightDrifterSync;
import epicsquid.roots.network.fx.MessageMindWardFX;
import epicsquid.roots.network.fx.MessageMindWardRingFX;
import epicsquid.roots.network.fx.MessagePetalShellBurstFX;
import epicsquid.roots.network.fx.MessageRadianceBeamFX;
import epicsquid.roots.network.fx.MessageSanctuaryBurstFX;
import epicsquid.roots.network.fx.MessageSanctuaryRingFX;
import epicsquid.roots.network.fx.MessageShatterBurstFX;
import epicsquid.roots.network.fx.MessageTimeStopStartFX;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {

  public static void registerMessages() {
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageImbueCompleteFX.MessageHolder.class, MessageImbueCompleteFX.class, Side.CLIENT);
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
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageMindWardRingFX.MessageHolder.class, MessageMindWardRingFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessageMindWardFX.MessageHolder.class, MessageMindWardFX.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessagePlayerGroveUpdate.MessageHolder.class, MessagePlayerGroveUpdate.class, Side.CLIENT);
    epicsquid.mysticallib.network.PacketHandler.registerMessage(MessagePlayerDataUpdate.MessageHolder.class, MessagePlayerDataUpdate.class, Side.CLIENT);

  }
}
