package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundDiscardBlockEntityAttachmentPacket(String attachmentType,
                                                            long blockEntity) implements IRootsPacket {
  public static final Type<ClientboundDiscardBlockEntityAttachmentPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_player_discard_block_entity_attachment"));
  public static final StreamCodec<FriendlyByteBuf, ClientboundDiscardBlockEntityAttachmentPacket> CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, ClientboundDiscardBlockEntityAttachmentPacket::attachmentType, ByteBufCodecs.VAR_LONG, ClientboundDiscardBlockEntityAttachmentPacket::blockEntity, ClientboundDiscardBlockEntityAttachmentPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.discardBlockEntityAttachment(attachmentType, blockEntity);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
