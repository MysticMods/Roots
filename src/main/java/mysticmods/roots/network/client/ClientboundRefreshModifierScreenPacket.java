package mysticmods.roots.network.client;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundRefreshModifierScreenPacket () implements IRootsPacket {
  private static final ClientboundRefreshModifierScreenPacket INSTANCE = new ClientboundRefreshModifierScreenPacket();

  public static final Type<ClientboundRefreshModifierScreenPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_refresh_modifier_screen"));
  public static final StreamCodec<ByteBuf, ClientboundRefreshModifierScreenPacket> CODEC = StreamCodec.unit(INSTANCE);

  public static ClientboundRefreshModifierScreenPacket getInstance() {
    return INSTANCE;
  }

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.refreshModifierScreen();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
