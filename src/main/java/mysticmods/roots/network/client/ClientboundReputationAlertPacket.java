package mysticmods.roots.network.client;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundReputationAlertPacket () implements IRootsPacket {
  public static final Type<ClientboundReputationAlertPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_reputation_alert_packet"));
  public static final ClientboundReputationAlertPacket INSTANCE = new ClientboundReputationAlertPacket();
  public static final StreamCodec<ByteBuf, ClientboundReputationAlertPacket> CODEC = StreamCodec.unit(INSTANCE);

  public static ClientboundReputationAlertPacket getInstance() {
    return INSTANCE;
  }

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.displayReputations();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
