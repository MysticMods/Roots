package mysticmods.roots.network.client.fx.screen;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SaturateScreenFXPacket(int entityId, int foodStartLevel, int foodNewLevel) implements IRootsPacket {
  public static final Type<SaturateScreenFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/saturate_screen"));
  public static final StreamCodec<FriendlyByteBuf, SaturateScreenFXPacket> CODEC = StreamCodec.composite(
      ByteBufCodecs.VAR_INT, SaturateScreenFXPacket::entityId,
      ByteBufCodecs.VAR_INT, SaturateScreenFXPacket::foodStartLevel,
      ByteBufCodecs.VAR_INT, SaturateScreenFXPacket::foodNewLevel,
      SaturateScreenFXPacket::new
  );


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.saturate(entityId, foodStartLevel, foodNewLevel);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
