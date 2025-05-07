package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AquaBubbleFXPacket(int entityId) implements IRootsPacket {
  public static final Type<AquaBubbleFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/aqua_bubble"));
  public static final StreamCodec<FriendlyByteBuf, AquaBubbleFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, AquaBubbleFXPacket::entityId, AquaBubbleFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.aquaBubble(entityId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
