package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.spell.DesaturateSpell;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record DesaturateScreenFX() implements IRootsPacket {
  public static final DesaturateScreenFX INSTANCE = new DesaturateScreenFX();
  public static final CustomPacketPayload.Type<DesaturateScreenFX> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/desaturate_screen"));
  public static final StreamCodec<FriendlyByteBuf, DesaturateScreenFX> CODEC = StreamCodec.unit(INSTANCE);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.desaturate();
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
