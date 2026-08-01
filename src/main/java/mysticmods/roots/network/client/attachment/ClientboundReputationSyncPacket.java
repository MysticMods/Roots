package mysticmods.roots.network.client.attachment;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.ReputationStorage;
import mysticmods.roots.api.network.ISyncPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundReputationSyncPacket(ReputationStorage storage) implements ISyncPacket<ReputationStorage> {
  public static final Type<ClientboundReputationSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_reputation_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundReputationSyncPacket> CODEC = StreamCodec.composite(ReputationStorage.STREAM_CODEC, ClientboundReputationSyncPacket::storage, ClientboundReputationSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.setReputationStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
