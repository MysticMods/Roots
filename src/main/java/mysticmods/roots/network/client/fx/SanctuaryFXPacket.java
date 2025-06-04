package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SanctuaryFXPacket(int entityId, int radius) implements IRootsPacket {
  public static final Type<SanctuaryFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/sanctuary"));
  public static final StreamCodec<FriendlyByteBuf, SanctuaryFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, SanctuaryFXPacket::entityId, ByteBufCodecs.VAR_INT, SanctuaryFXPacket::radius, SanctuaryFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.sanctuary(entityId, radius);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
