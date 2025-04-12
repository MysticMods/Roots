package mysticmods.roots.network.server;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.IRootsPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ServerboundOpenPouchPacket () implements IRootsPacket {
  public static final ServerboundOpenPouchPacket INSTANCE = new ServerboundOpenPouchPacket();
  public static final Type<ServerboundOpenPouchPacket> TYPE = new Type<>(RootsAPI.rl("server_bound_open_pouch"));
  public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenPouchPacket> CODEC =
      StreamCodec.unit(INSTANCE);


  @Override
  public void handle(IPayloadContext context) {
    ServerNetworkHooks.openPouch(context.player());
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
