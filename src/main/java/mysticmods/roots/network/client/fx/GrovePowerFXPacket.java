package mysticmods.roots.network.client.fx;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.grove.Grove;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GrovePowerFXPacket(BlockPos origin, Grove originType,
                                 Object2IntMap<BlockPos> counts) implements IRootsPacket {
  public final static Type<GrovePowerFXPacket> TYPE = new Type<>(RootsAPI.rl("grove_power_fx_packet"));
  public static final StreamCodec<RegistryFriendlyByteBuf, GrovePowerFXPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, GrovePowerFXPacket::origin, Grove.STREAM_CODEC, GrovePowerFXPacket::originType, ByteBufCodecs.map(Object2IntOpenHashMap::new, BlockPos.STREAM_CODEC, ByteBufCodecs.VAR_INT), GrovePowerFXPacket::counts, GrovePowerFXPacket::new);

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.grovePowerFX(origin, originType, counts);
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }
}
