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

public record ClientboundOpenLibraryPacket(InteractionHand hand) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundOpenLibraryPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_open_library"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenLibraryPacket> CODEC = StreamCodec.composite(ExtraStreamCodecs.INTERACTION_HAND_CODEC, ClientboundOpenLibraryPacket::hand, ClientboundOpenLibraryPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.openLibrary(this.hand);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
