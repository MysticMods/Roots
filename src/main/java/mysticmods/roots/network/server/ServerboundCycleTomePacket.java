package mysticmods.roots.network.server;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundCycleTomePacket() implements IRootsPacket {
  public static final ServerboundCycleTomePacket INSTANCE = new ServerboundCycleTomePacket();
  public static final CustomPacketPayload.Type<ServerboundCycleTomePacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("server_bound_cycle_tome_packet"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundCycleTomePacket> CODEC =
      StreamCodec.unit(INSTANCE);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.cycleTome(context.player());
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
