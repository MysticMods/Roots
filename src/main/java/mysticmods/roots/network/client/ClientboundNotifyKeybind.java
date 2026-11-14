package mysticmods.roots.network.client;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RootsClientHooks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundNotifyKeybind(Item item) implements IRootsPacket {
  public static final Type<ClientboundNotifyKeybind> TYPE = new Type<>(RootsAPI.rl("client_bound_notify_keybind"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNotifyKeybind> CODEC = ByteBufCodecs.registry(Registries.ITEM)
      .map(ClientboundNotifyKeybind::new, ClientboundNotifyKeybind::item);


  @Override
  public void handle(IPayloadContext context) {
    RootsClientHooks.notifyItem(item);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
