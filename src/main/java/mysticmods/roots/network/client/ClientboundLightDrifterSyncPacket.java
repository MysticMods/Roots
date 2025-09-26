package mysticmods.roots.network.client;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.LightDrifterStorage;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Optional;
import java.util.UUID;

public record ClientboundLightDrifterSyncPacket (int entityId) implements IRootsPacket {
  public static final StreamCodec<ByteBuf, ClientboundLightDrifterSyncPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, ClientboundLightDrifterSyncPacket::entityId,
      ClientboundLightDrifterSyncPacket::new);
  public static final Type<ClientboundLightDrifterSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_light_drifter_sync"));

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.setLightDrifterSync(entityId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
