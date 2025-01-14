package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.attachment.SnapshotStorage;
import mysticmods.roots.network.ISyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientBoundReputationSyncPacket(ReputationStorage storage) implements ISyncPacket<ReputationStorage> {
  public static final Type<ClientBoundReputationSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_reputation_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientBoundReputationSyncPacket> CODEC = StreamCodec.composite(ReputationStorage.STREAM_CODEC, ClientBoundReputationSyncPacket::storage, ClientBoundReputationSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.setReputationStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
