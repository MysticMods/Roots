package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.network.ISyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundEntitySnapshotSyncPacket(SnapshotStorage storage,
                                                  int entity) implements ISyncPacket<SnapshotStorage> {
  public static final Type<ClientboundEntitySnapshotSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_entity_snapshot_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundEntitySnapshotSyncPacket> CODEC = StreamCodec.composite(SnapshotStorage.STREAM_CODEC, ClientboundEntitySnapshotSyncPacket::storage, ByteBufCodecs.VAR_INT, ClientboundEntitySnapshotSyncPacket::entity, ClientboundEntitySnapshotSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.setEntitySnapshot(this.entity, this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
