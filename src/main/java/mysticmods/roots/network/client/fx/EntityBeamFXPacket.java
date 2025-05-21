package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.client.particle.Beam;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record EntityBeamFXPacket (int entityId1, int entityId2) implements IRootsPacket {
  public static final CustomPacketPayload.Type<EntityBeamFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/entity_beam"));
  public static final StreamCodec<FriendlyByteBuf, EntityBeamFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, EntityBeamFXPacket::entityId1, ByteBufCodecs.VAR_INT, EntityBeamFXPacket::entityId2, EntityBeamFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.addEntityBeam(entityId1, entityId2);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
