package mysticmods.roots.network.client.fx;

import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GrowthAmplifierFXPacket(Vec3 target, BlockPos amplifier) implements IRootsPacket {
  public static final CustomPacketPayload.Type<GrowthAmplifierFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/growth_amplifier"));
  public static final StreamCodec<FriendlyByteBuf, GrowthAmplifierFXPacket> CODEC = StreamCodec.composite(ExtraStreamCodecs.VEC3, GrowthAmplifierFXPacket::target, BlockPos.STREAM_CODEC, GrowthAmplifierFXPacket::amplifier, GrowthAmplifierFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.growthAmplifierGrew(amplifier, target);
  }

  @Override
  public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
