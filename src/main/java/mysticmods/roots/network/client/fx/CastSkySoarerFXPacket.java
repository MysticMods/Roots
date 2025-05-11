package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastSkySoarerFXPacket(int entityId, int duration) implements IRootsPacket {
  public static final Type<CastSkySoarerFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/cast_sky_soarer"));
  public static final StreamCodec<FriendlyByteBuf, CastSkySoarerFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CastSkySoarerFXPacket::entityId, ByteBufCodecs.VAR_INT, CastSkySoarerFXPacket::duration, CastSkySoarerFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castSkySorarer(entityId, duration);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
