package mysticmods.roots.network.client;

import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.api.capability.HerbCapability;
import mysticmods.roots.client.ClientTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundGrantSyncPacket {
  private final GrantCapability.SerializedGrantRecord record;

  public ClientBoundGrantSyncPacket(GrantCapability.SerializedGrantRecord record) {
    this.record = record;
  }

  public ClientBoundGrantSyncPacket(FriendlyByteBuf buf) {
    record = GrantCapability.fromNetwork(buf);
  }

  public void encode(FriendlyByteBuf buf) {
    record.toNetwork(buf);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientBoundGrantSyncPacket packet, Supplier<NetworkEvent.Context> context) {
    ClientTicker.setGrantRecord(packet.record);
  }
}
