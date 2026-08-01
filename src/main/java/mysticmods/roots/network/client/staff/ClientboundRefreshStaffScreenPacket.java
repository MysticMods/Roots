package mysticmods.roots.network.client.staff;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ClientboundRefreshStaffScreenPacket() implements IRootsPacket {
  private static final ClientboundRefreshStaffScreenPacket INSTANCE = new ClientboundRefreshStaffScreenPacket();

  public static final Type<ClientboundRefreshStaffScreenPacket> TYPE = new Type<>(RootsAPI.rl("client_bound_refresh_staff_screen"));
  public static final StreamCodec<ByteBuf, ClientboundRefreshStaffScreenPacket> CODEC = StreamCodec.unit(INSTANCE);

  public static ClientboundRefreshStaffScreenPacket getInstance() {
    return INSTANCE;
  }

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.refreshStaffScreen();
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
