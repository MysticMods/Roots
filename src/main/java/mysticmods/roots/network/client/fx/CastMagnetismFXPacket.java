package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastMagnetismFXPacket(int entityId) implements IRootsPacket {
  public static final Type<CastMagnetismFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/cast_magnetism"));
  public static final StreamCodec<FriendlyByteBuf, CastMagnetismFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CastMagnetismFXPacket::entityId, CastMagnetismFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castMagnetism(entityId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
