package mysticmods.roots.network.server;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundClearContainerPacket(BlockPos pos) implements IRootsPacket {
  public static final Type<ServerboundClearContainerPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_clear_container_packet"));
  public static final StreamCodec<ByteBuf, ServerboundClearContainerPacket> CODEC = BlockPos.STREAM_CODEC.map(ServerboundClearContainerPacket::new, ServerboundClearContainerPacket::pos);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.clearContainer(context.player(), pos);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
