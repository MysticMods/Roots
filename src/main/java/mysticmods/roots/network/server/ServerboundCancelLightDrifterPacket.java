package mysticmods.roots.network.server;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundCancelLightDrifterPacket () implements IRootsPacket {
  public static final ServerboundCancelLightDrifterPacket INSTANCE = new ServerboundCancelLightDrifterPacket();
  public static final Type<ServerboundCancelLightDrifterPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_cancel_light_drifter_packet"));
  public static final StreamCodec<ByteBuf, ServerboundCancelLightDrifterPacket> CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.cancelLightDrifter(context.player());
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
