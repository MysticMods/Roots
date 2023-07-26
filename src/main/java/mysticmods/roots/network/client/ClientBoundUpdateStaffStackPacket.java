package mysticmods.roots.network.client;

import mysticmods.roots.hooks.SafeClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientBoundUpdateStaffStackPacket {
  private final ItemStack stack;

  public ClientBoundUpdateStaffStackPacket(ItemStack stack) {
    this.stack = stack;
  }

  public ClientBoundUpdateStaffStackPacket(FriendlyByteBuf buf) {
    this.stack = buf.readItem();
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeItemStack(stack, false);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ClientBoundUpdateStaffStackPacket packet, Supplier<NetworkEvent.Context> context) {
    SafeClientHooks.updateGui(packet.stack);
  }
}
