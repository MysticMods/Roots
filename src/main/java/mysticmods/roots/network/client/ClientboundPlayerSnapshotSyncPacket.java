package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.network.ISyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundPlayerSnapshotSyncPacket(SnapshotStorage storage) implements ISyncPacket<SnapshotStorage> {
  public static final CustomPacketPayload.Type<ClientboundPlayerSnapshotSyncPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_bound_player_snapshot_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerSnapshotSyncPacket> CODEC = StreamCodec.composite(SnapshotStorage.STREAM_CODEC, ClientboundPlayerSnapshotSyncPacket::storage, ClientboundPlayerSnapshotSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.setSnapshotStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
