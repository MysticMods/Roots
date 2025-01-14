package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.GrantStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.network.ISyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientBoundGrantSyncPacket(GrantStorage storage) implements ISyncPacket<GrantStorage> {
  public static final Type<ClientBoundGrantSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_snapshot_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundGrantSyncPacket> CODEC = StreamCodec.composite(GrantStorage.STREAM_CODEC, ClientBoundGrantSyncPacket::storage, ClientBoundGrantSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.setGrantStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
