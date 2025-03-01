package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.network.IRootsPacket;
import mysticmods.roots.network.client.ClientFXHandlers;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GrowthFXPacket(Vec3 location) implements IRootsPacket {
  public static final Type<GrowthFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/growth"));
  public static final StreamCodec<FriendlyByteBuf, GrowthFXPacket> CODEC = StreamCodec.composite(ExtraStreamCodecs.VEC3, GrowthFXPacket::location, GrowthFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.growth(location);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
