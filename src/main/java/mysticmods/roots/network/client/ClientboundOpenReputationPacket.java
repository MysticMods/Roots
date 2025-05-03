package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.client.RootsClientHooks;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundOpenReputationPacket () implements IRootsPacket {
  public static final ClientboundOpenReputationPacket PACKET = new ClientboundOpenReputationPacket();
  public static final CustomPacketPayload.Type<ClientboundOpenReputationPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_open_reputation"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenReputationPacket> CODEC = StreamCodec.unit(PACKET);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.openReputation();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
