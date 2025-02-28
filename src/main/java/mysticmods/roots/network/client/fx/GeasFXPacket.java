package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.IRootsPacket;
import mysticmods.roots.network.client.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// Unused
public record GeasFXPacket(int entityId) implements IRootsPacket {
  public static final CustomPacketPayload.Type<GeasFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/geas"));
  public static final StreamCodec<FriendlyByteBuf, GeasFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, GeasFXPacket::entityId, GeasFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.geas(entityId);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
