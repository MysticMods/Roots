package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.config.ConfigManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundReputationMessagePacket(Grove grove, int adjustment) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundReputationMessagePacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_bound_reputation_message"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundReputationMessagePacket> CODEC = StreamCodec.composite(ByteBufCodecs.registry(RootsRegistries.Keys.GROVES), ClientboundReputationMessagePacket::grove, ByteBufCodecs.VAR_INT, ClientboundReputationMessagePacket::adjustment, ClientboundReputationMessagePacket::new);

  @Override
  public void handle(IPayloadContext context) {
    if (ConfigManager.SUPPRESS_REPUTATION_CHANGES.get()) {
      return;
    }
    String key;
    if (adjustment() < 0) {
      key = "roots.reputation.decreased";
    } else {
      key = "roots.reputation.increased";
    }
    // TODO: Ranks, etc
    context.player().displayClientMessage(Component.translatable(key, grove().getStyledName(), adjustment()), true);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
