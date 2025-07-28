package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GrowthFXPacket(BlockPos location) implements IRootsPacket {
  public static final Type<GrowthFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/growth"));
  public static final StreamCodec<FriendlyByteBuf, GrowthFXPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, GrowthFXPacket::location, GrowthFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.growth(location);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
