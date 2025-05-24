package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record CastLifeDrainFXPacket(int entityId, double distance, int angle) implements IRootsPacket {
  public static final Type<CastLifeDrainFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/cast_life_drain"));
  public static final StreamCodec<FriendlyByteBuf, CastLifeDrainFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, CastLifeDrainFXPacket::entityId, ByteBufCodecs.DOUBLE, CastLifeDrainFXPacket::distance, ByteBufCodecs.VAR_INT, CastLifeDrainFXPacket::angle, CastLifeDrainFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.castLifeDrain(entityId, distance, angle);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
