package mysticmods.roots.network.client;

import mysticmods.roots.api.capability.GrantCapability;
import mysticmods.roots.client.ClientHooks;
import mysticmods.roots.client.ClientTicker;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundOpenLibraryPacket {
  private final InteractionHand hand;

  public ClientBoundOpenLibraryPacket(InteractionHand hand) {
    this.hand = hand;
  }

  public ClientBoundOpenLibraryPacket(FriendlyByteBuf buf) {
    this.hand = buf.readEnum(InteractionHand.class);
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeEnum(hand);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientBoundOpenLibraryPacket packet, Supplier<NetworkEvent.Context> context) {
    ClientHooks.openGui(packet.hand);
  }
}
