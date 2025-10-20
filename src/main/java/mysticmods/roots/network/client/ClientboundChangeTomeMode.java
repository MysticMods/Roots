package mysticmods.roots.network.client;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundChangeTomeMode() implements IRootsPacket {
  public static final Type<ClientboundChangeTomeMode> TYPE = new Type<>(RootsAPI.rl("client_bound_change_tome_mode"));
  public static final ClientboundChangeTomeMode INSTANCE = new ClientboundChangeTomeMode();
  public static final StreamCodec<ByteBuf, ClientboundChangeTomeMode> CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.showTomeTooltip();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
