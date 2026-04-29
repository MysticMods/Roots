package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundPouchPickUpHerbPacket(int entityId, ItemStack stack) implements IRootsPacket {
  public static final CustomPacketPayload.Type<ClientboundPouchPickUpHerbPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_pouch_pick_up_herb"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPouchPickUpHerbPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, ClientboundPouchPickUpHerbPacket::entityId,
      ItemStack.STREAM_CODEC, ClientboundPouchPickUpHerbPacket::stack,
      ClientboundPouchPickUpHerbPacket::new
  );

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.pouchPickUpHerb(entityId, stack);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
