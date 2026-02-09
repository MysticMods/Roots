package mysticmods.roots.network.server;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundFakeMenuPacket(BlockPos pos) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ServerboundFakeMenuPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("server_bound_fake_menu_packet"));
  public static final StreamCodec<ByteBuf, ServerboundFakeMenuPacket> CODEC = BlockPos.STREAM_CODEC.map(ServerboundFakeMenuPacket::new, ServerboundFakeMenuPacket::pos);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.fakeMenu(context.player(), pos);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
