package mysticmods.roots.network.server.staff;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.network.client.staff.ClientboundRefreshStaffScreenPacket;
import mysticmods.roots.network.server.ServerNetworkHooks;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundCycleStaffSpellPacket(InteractionHand hand) implements IRootsPacket {
  public static final Type<ServerboundCycleStaffSpellPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_cycle_staff_spell"));
  public static final StreamCodec<ByteBuf, ServerboundCycleStaffSpellPacket> CODEC =
      StreamCodec.composite(
          ExtraStreamCodecs.INTERACTION_HAND_CODEC, ServerboundCycleStaffSpellPacket::hand,
          ServerboundCycleStaffSpellPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.cycleStaffSpell(context.player(), this.hand);
    context.player().inventoryMenu.sendAllDataToRemote();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
