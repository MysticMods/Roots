package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SpiralFXPacket (BlockPos position, double radius, double angle, int color1, int color2) implements IRootsPacket {
  public static final Type<SpiralFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/spiral"));
  public static final StreamCodec<FriendlyByteBuf, SpiralFXPacket> CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, SpiralFXPacket::position, ByteBufCodecs.DOUBLE, SpiralFXPacket::radius, ByteBufCodecs.DOUBLE, SpiralFXPacket::angle, ByteBufCodecs.INT, SpiralFXPacket::color1, ByteBufCodecs.INT, SpiralFXPacket::color2, SpiralFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.spiral(position, radius, angle, color1, color2);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
