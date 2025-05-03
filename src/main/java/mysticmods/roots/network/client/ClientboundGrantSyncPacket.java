package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.api.network.ISyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundGrantSyncPacket(GrantStorage storage) implements ISyncPacket<GrantStorage> {
  public static final Type<ClientboundGrantSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_grant_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundGrantSyncPacket> CODEC = StreamCodec.composite(GrantStorage.STREAM_CODEC, ClientboundGrantSyncPacket::storage, ClientboundGrantSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.setGrantStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
