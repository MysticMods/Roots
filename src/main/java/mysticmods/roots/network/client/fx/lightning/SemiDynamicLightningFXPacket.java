package mysticmods.roots.network.client.fx.lightning;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record SemiDynamicLightningFXPacket(LightningPreset preset, int renderer, int start, Vec3 end,
                                           int segments) implements IRootsPacket {
  public static final Type<SemiDynamicLightningFXPacket> TYPE = new Type<>(RootsAPI.rl("client_fx/lightning/semidynamic"));
  public static final StreamCodec<ByteBuf, SemiDynamicLightningFXPacket> CODEC = StreamCodec.composite(
      LightningPreset.STREAM_CODEC, SemiDynamicLightningFXPacket::preset,
      ByteBufCodecs.VAR_INT, SemiDynamicLightningFXPacket::renderer,
      ByteBufCodecs.VAR_INT, SemiDynamicLightningFXPacket::start,
      ExtraStreamCodecs.VEC3, SemiDynamicLightningFXPacket::end,
      ByteBufCodecs.VAR_INT, SemiDynamicLightningFXPacket::segments,
      SemiDynamicLightningFXPacket::new
  );

  @NotNull
  @Override
  public CustomPacketPayload.Type<SemiDynamicLightningFXPacket> type() {
    return TYPE;
  }

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.createBolt(renderer, preset, start, end, segments);
  }

}