package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientBoundSnapshotSyncPacket(SnapshotStorage storage) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientBoundSnapshotSyncPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_bound_snapshot_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundSnapshotSyncPacket> CODEC = StreamCodec.composite(SnapshotStorage.STREAM_CODEC, ClientBoundSnapshotSyncPacket::storage, ClientBoundSnapshotSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.setSnapshotStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
