package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record HealFXPacket(int entityId, float amount) implements IRootsPacket {
  public static final Type<HealFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/heal"));
  public static final StreamCodec<FriendlyByteBuf, HealFXPacket> CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, HealFXPacket::entityId, ByteBufCodecs.FLOAT, HealFXPacket::amount, HealFXPacket::new);


  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.heal(entityId, amount);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
