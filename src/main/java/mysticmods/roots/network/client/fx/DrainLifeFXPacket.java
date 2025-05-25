package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DrainLifeFXPacket(int entityId, int casterId) implements IRootsPacket {
  public static final Type<DrainLifeFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/drain_life"));
  public static final StreamCodec<FriendlyByteBuf, DrainLifeFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, DrainLifeFXPacket::entityId, ByteBufCodecs.VAR_INT, DrainLifeFXPacket::casterId, DrainLifeFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.drainLife(entityId, casterId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
