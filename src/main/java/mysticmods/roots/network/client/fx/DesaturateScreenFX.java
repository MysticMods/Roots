package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DesaturateScreenFX(float heartsStart, float heartsNow, int foodStartLevel, int foodNewLevel) implements IRootsPacket {
  public static final CustomPacketPayload.Type<DesaturateScreenFX> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/desaturate_screen"));
  public static final StreamCodec<FriendlyByteBuf, DesaturateScreenFX> CODEC = StreamCodec.composite(
      ByteBufCodecs.FLOAT, DesaturateScreenFX::heartsStart,
      ByteBufCodecs.FLOAT, DesaturateScreenFX::heartsNow,
      ByteBufCodecs.VAR_INT, DesaturateScreenFX::foodStartLevel,
      ByteBufCodecs.VAR_INT, DesaturateScreenFX::foodNewLevel,
      DesaturateScreenFX::new
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
