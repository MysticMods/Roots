package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundSyncGeasPacket(int entityId, boolean value) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundSyncGeasPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_bound_sync_geas"));
  public static final StreamCodec<FriendlyByteBuf, ClientboundSyncGeasPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSyncGeasPacket::entityId, ByteBufCodecs.BOOL, ClientboundSyncGeasPacket::value, ClientboundSyncGeasPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.syncGeas(entityId, value);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
