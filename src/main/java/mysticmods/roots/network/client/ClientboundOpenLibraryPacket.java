package mysticmods.roots.network.client;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import javax.annotation.Nullable;
import java.util.Optional;

public record ClientboundOpenLibraryPacket(@Nullable InteractionHand hand, int inventorySlot) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundOpenLibraryPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_open_library"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenLibraryPacket> CODEC = StreamCodec.composite(ByteBufCodecs.optional(ExtraStreamCodecs.INTERACTION_HAND_CODEC), o -> Optional.ofNullable(o.hand), ByteBufCodecs.VAR_INT, o -> o.inventorySlot, ClientboundOpenLibraryPacket::new);

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  public ClientboundOpenLibraryPacket(Optional<InteractionHand> hand, int inventorySlot) {
    this(hand.orElse(null), inventorySlot);
  }

  public ClientboundOpenLibraryPacket(InteractionHand hand) {
    this(hand, -1);
  }

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.openLibrary(this.hand, this.inventorySlot);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
