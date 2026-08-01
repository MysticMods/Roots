package mysticmods.roots.network.client.attachment;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.attachment.CooldownStorage;
import mysticmods.roots.api.network.ISyncPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundCooldownSyncPacket(CooldownStorage storage) implements ISyncPacket<CooldownStorage> {
  public static final Type<ClientboundCooldownSyncPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_cooldown_sync"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCooldownSyncPacket> CODEC = StreamCodec.composite(CooldownStorage.STREAM_CODEC, ClientboundCooldownSyncPacket::storage, ClientboundCooldownSyncPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.setCooldownStorage(this.storage);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
