package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundDiscardEntityAttachmentPacket(String attachmentType, int entity) implements IRootsPacket {
  public static final Type<ClientboundDiscardEntityAttachmentPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_player_discard_entity_attachment"));
  public static final StreamCodec<FriendlyByteBuf, ClientboundDiscardEntityAttachmentPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundDiscardEntityAttachmentPacket::attachmentType, ByteBufCodecs.VAR_INT, ClientboundDiscardEntityAttachmentPacket::entity, ClientboundDiscardEntityAttachmentPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.discardEntityAttachment(attachmentType, entity);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
