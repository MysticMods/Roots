package mysticmods.roots.network.server;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundSetSpellDataPacket(InteractionHand hand, int index, int value) implements IRootsPacket {
  public static final Type<ServerboundSetSpellDataPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_set_spell_data"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundSetSpellDataPacket> CODEC =
      StreamCodec.composite(
          ExtraStreamCodecs.INTERACTION_HAND_CODEC, o -> o.hand,
          ByteBufCodecs.VAR_INT, o -> o.index,
          ByteBufCodecs.VAR_INT, o -> o.value,
          ServerboundSetSpellDataPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.setSpellData(context.player(), this.hand, this.index, this.value);
    context.player().inventoryMenu.sendAllDataToRemote();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
