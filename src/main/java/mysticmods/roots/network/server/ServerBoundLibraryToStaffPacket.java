package mysticmods.roots.network.server;

import mysticmods.roots.ServerHooks;
import mysticmods.roots.api.registry.Registries;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerBoundLibraryToStaffPacket {
  private final Spell spell;
  private final int slot;
  private final InteractionHand hand;

  public ServerBoundLibraryToStaffPacket(InteractionHand hand, int slot, Spell spell) {
    this.hand = hand;
    this.slot = slot;
    this.spell = spell;
  }

  public ServerBoundLibraryToStaffPacket(FriendlyByteBuf buf) {
    this.hand = buf.readEnum(InteractionHand.class);
    this.slot = buf.readVarInt();
    this.spell = Registries.SPELL_REGISTRY.get().getValue(buf.readVarInt());
  }

  public void encode(FriendlyByteBuf buf) {
    buf.writeEnum(hand);
    buf.writeVarInt(slot);
    buf.writeVarInt(Registries.SPELL_REGISTRY.get().getID(spell));
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> handle(this, context));
    context.get().setPacketHandled(true);
  }

  private static void handle(ServerBoundLibraryToStaffPacket packet, Supplier<NetworkEvent.Context> context) {
    ServerHooks.insertSpell(context.get().getSender(), packet.hand, packet.slot, packet.spell);
  }
}
