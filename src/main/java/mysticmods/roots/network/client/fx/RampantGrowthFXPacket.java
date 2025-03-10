package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record RampantGrowthFXPacket(BlockPos location) implements IRootsPacket {
  public static final Type<RampantGrowthFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/rampant_growth"));
  public static final StreamCodec<FriendlyByteBuf, RampantGrowthFXPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, RampantGrowthFXPacket::location, RampantGrowthFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.rampantGrowth(location);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
