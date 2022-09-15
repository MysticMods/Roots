package mysticmods.roots.network.client;

import mysticmods.roots.api.capability.HerbCapability;
import mysticmods.roots.client.ClientTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundHerbSyncPacket {
  private final HerbCapability.SerializedHerbRecord record;

  public ClientBoundHerbSyncPacket(HerbCapability.SerializedHerbRecord record) {
    this.record = record;
  }

  public ClientBoundHerbSyncPacket(FriendlyByteBuf buf) {
    record = HerbCapability.fromNetwork(buf);
  }

  public void encode(FriendlyByteBuf buf) {
    record.toNetwork(buf);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientBoundHerbSyncPacket packet, Supplier<NetworkEvent.Context> context) {
    ClientTicker.setHerbRecord(packet.record);
  }
}
