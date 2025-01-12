package mysticmods.roots.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IRootsPacket extends CustomPacketPayload {
  void handle (IPayloadContext context);
}
