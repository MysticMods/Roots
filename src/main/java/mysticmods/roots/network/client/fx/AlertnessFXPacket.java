package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record AlertnessFXPacket(int entityId) implements IRootsPacket {
  public static final CustomPacketPayload.Type<AlertnessFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/alertness"));
  public static final StreamCodec<FriendlyByteBuf, AlertnessFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, AlertnessFXPacket::entityId, AlertnessFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.alert(entityId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
