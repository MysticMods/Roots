package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record CastShatterFX(int entityId, List<BlockPos> positions) implements IRootsPacket {
  public static final Type<CastShatterFX> TYPE = new Type<>(RootsAPI.rl("client_fx/cast_shatter"));
  public static final StreamCodec<FriendlyByteBuf, CastShatterFX> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CastShatterFX::entityId, BlockPos.STREAM_CODEC.apply(ByteBufCodecs.list()), CastShatterFX::positions, CastShatterFX::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castShatter(entityId, positions);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
