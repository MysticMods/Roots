package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DesaturateScreenFXPacket(float heartsStart, float heartsNow, int foodStartLevel, int foodNewLevel) implements IRootsPacket {
  public static final CustomPacketPayload.Type<DesaturateScreenFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/desaturate_screen"));
  public static final StreamCodec<FriendlyByteBuf, DesaturateScreenFXPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT, DesaturateScreenFXPacket::heartsStart,
      ByteBufCodecs.FLOAT, DesaturateScreenFXPacket::heartsNow,
      ByteBufCodecs.VAR_INT, DesaturateScreenFXPacket::foodStartLevel,
      ByteBufCodecs.VAR_INT, DesaturateScreenFXPacket::foodNewLevel,
      DesaturateScreenFXPacket::new
  );


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.desaturate(heartsStart, heartsNow, foodStartLevel, foodNewLevel);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
