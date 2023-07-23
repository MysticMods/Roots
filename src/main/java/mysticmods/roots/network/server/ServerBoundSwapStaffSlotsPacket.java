package mysticmods.roots.network.server;

import mysticmods.roots.ServerHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundSwapStaffSlotsPacket {
  private final int slot1, slot2;
  private final InteractionHand hand;

  public ServerBoundSwapStaffSlotsPacket(InteractionHand hand, int slot1, int slot2) {
    this.hand = hand;
    this.slot1 = slot1;
    this.slot2 = slot2;
  }

  public ServerBoundSwapStaffSlotsPacket(FriendlyByteBuf buf) {
    this.hand = buf.readEnum(InteractionHand.class);
    this.slot1 = buf.readVarInt();
    this.slot2 = buf.readVarInt();
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeEnum(hand);
    buf.writeVarInt(slot1);
    buf.writeVarInt(slot2);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ServerBoundSwapStaffSlotsPacket packet, Supplier<NetworkEvent.Context> context) {
    ServerHooks.swapSlots(context.get().getSender(), packet.hand, packet.slot1, packet.slot2);
  }
}
