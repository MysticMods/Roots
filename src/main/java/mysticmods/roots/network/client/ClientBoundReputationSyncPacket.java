package mysticmods.roots.network.client;

import mysticmods.roots.api.capability.ReputationCapability;
import mysticmods.roots.client.ClientTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundReputationSyncPacket {
  private final ReputationCapability.SerializedReputationRecord record;

  public ClientBoundReputationSyncPacket(ReputationCapability.SerializedReputationRecord record) {
    this.record = record;
  }

  public ClientBoundReputationSyncPacket(FriendlyByteBuf buf) {
    record = ReputationCapability.fromNetwork(buf);
  }

  public void encode(FriendlyByteBuf buf) {
    record.toNetwork(buf);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientBoundReputationSyncPacket packet, Supplier<NetworkEvent.Context> context) {
    ClientTicker.setReputationRecord(packet.record);
  }
}
