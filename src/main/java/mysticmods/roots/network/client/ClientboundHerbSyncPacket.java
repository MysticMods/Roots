package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.HerbStorage;
import mysticmods.roots.network.ISyncPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundHerbSyncPacket(HerbStorage storage) implements ISyncPacket<HerbStorage> {
  public static final Type<ClientboundHerbSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_herb_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundHerbSyncPacket> CODEC = StreamCodec.composite(HerbStorage.STREAM_CODEC, ClientboundHerbSyncPacket::storage, ClientboundHerbSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientNetworkHandlers.setHerbStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
