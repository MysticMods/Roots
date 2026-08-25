package mysticmods.roots.network.client;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundClearHighlightPacket() implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundClearHighlightPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("clear_highlight"));
  public static final ClientboundClearHighlightPacket INSTANCE = new ClientboundClearHighlightPacket();
  public static final StreamCodec<ByteBuf, ClientboundClearHighlightPacket> CODEC = StreamCodec.unit(INSTANCE);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.clearTooltipItem();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
