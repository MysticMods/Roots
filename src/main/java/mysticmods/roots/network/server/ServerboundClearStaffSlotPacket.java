package mysticmods.roots.network.server;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.network.client.ClientboundRefreshModifierScreenPacket;
import mysticmods.roots.network.client.ClientboundRefreshStaffScreenPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ServerboundClearStaffSlotPacket(@Nullable InteractionHand hand, int inventorySlot,
                                              int staffSlot) implements IRootsPacket {
  public static final Type<ServerboundClearStaffSlotPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_clear_staff_slot"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundClearStaffSlotPacket> CODEC =
      StreamCodec.composite(
          ByteBufCodecs.optional(ExtraStreamCodecs.INTERACTION_HAND_CODEC), o -> Optional.ofNullable(o.hand),
          ByteBufCodecs.VAR_INT, o -> o.inventorySlot,
          ByteBufCodecs.VAR_INT, o -> o.staffSlot,
          ServerboundClearStaffSlotPacket::new);


  private ServerboundClearStaffSlotPacket(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<InteractionHand> hand, int inventorySlot, int staffSlot) {
    this(hand.orElse(null), inventorySlot, staffSlot);
  }

  public ServerboundClearStaffSlotPacket(InteractionHand hand, int staffSlot) {
    this(hand, -1, staffSlot);
  }

  public ServerboundClearStaffSlotPacket(int inventorySlot, int staffSlot) {
    this((InteractionHand) null, inventorySlot, staffSlot);
  }

  @Override

  public void handle(IPayloadContext context) {
    ServerNetworkHooks.clearSpellSlot(context.player(), this.hand, this.inventorySlot, this.staffSlot);
    context.player().inventoryMenu.sendAllDataToRemote();
    PacketDistributor.sendToPlayer((ServerPlayer) context.player(), ClientboundRefreshStaffScreenPacket.getInstance());
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
