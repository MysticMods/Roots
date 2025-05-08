package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DisarmFXPacket(int entityId) implements IRootsPacket {
  public static final Type<DisarmFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/disarm"));
  public static final StreamCodec<FriendlyByteBuf, DisarmFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, DisarmFXPacket::entityId, DisarmFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.disarm(entityId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
