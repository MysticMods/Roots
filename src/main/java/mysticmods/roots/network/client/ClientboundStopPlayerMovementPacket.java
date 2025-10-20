package mysticmods.roots.network.client;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundStopPlayerMovementPacket() implements IRootsPacket {
  public static final Type<ClientboundStopPlayerMovementPacket> TYPE =
      new Type<>(RootsAPI.rl("client_bound_stop_player_movement_packet"));
  public static final ClientboundStopPlayerMovementPacket INSTANCE = new ClientboundStopPlayerMovementPacket();
  public static final StreamCodec<ByteBuf, ClientboundStopPlayerMovementPacket> CODEC = StreamCodec.unit(INSTANCE);


  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.stopPlayerMovement();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
