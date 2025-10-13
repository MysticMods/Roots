package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// TODO: Adjust visual based on the actual effect
public record AcidCloudFXPacket(int entityId) implements IRootsPacket {
  public static final Type<AcidCloudFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/acid_cloud"));
  public static final StreamCodec<FriendlyByteBuf, AcidCloudFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, AcidCloudFXPacket::entityId, AcidCloudFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.acidCloud(entityId);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
