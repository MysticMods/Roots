package mysticmods.roots.network.client.fx.lightning;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.client.particle.bolt.LightningPreset;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record DynamicLightningFXPacket(LightningPreset preset, int renderer, int start, int end,
                                       int segments) implements IRootsPacket {
  public static final Type<DynamicLightningFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/lightning/dynamic"));
  public static final StreamCodec<ByteBuf, DynamicLightningFXPacket> CODEC = StreamCodec.composite(
      LightningPreset.STREAM_CODEC, DynamicLightningFXPacket::preset,
      ByteBufCodecs.VAR_INT, DynamicLightningFXPacket::renderer,
      ByteBufCodecs.VAR_INT, DynamicLightningFXPacket::start,
      ByteBufCodecs.VAR_INT, DynamicLightningFXPacket::end,
      ByteBufCodecs.VAR_INT, DynamicLightningFXPacket::segments,
      DynamicLightningFXPacket::new
  );

  @NotNull
  @Override
  public CustomPacketPayload.Type<DynamicLightningFXPacket> type() {
    return TYPE;
  }

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.createBolt(renderer, preset, start, end, segments);
  }

}