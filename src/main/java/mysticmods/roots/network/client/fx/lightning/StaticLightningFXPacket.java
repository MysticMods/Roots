package mysticmods.roots.network.client.fx.lightning;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.network.ClientFXHandlers;
import mysticmods.roots.client.particle.bolt.LightningPreset;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

public record StaticLightningFXPacket(LightningPreset preset, int renderer, Vec3 start, Vec3 end,
                                      int segments) implements IRootsPacket {
  public StaticLightningFXPacket(LightningPreset preset, int renderer, Vec3 start, Vec3 end) {
    this(preset, renderer, start, end, (int) (Math.sqrt(start.distanceTo(end) * 5)));
  }

  public static final CustomPacketPayload.Type<StaticLightningFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("client_fx/lightning/static"));
  public static final StreamCodec<ByteBuf, StaticLightningFXPacket> CODEC = StreamCodec.composite(
      LightningPreset.STREAM_CODEC, StaticLightningFXPacket::preset,
      ByteBufCodecs.VAR_INT, StaticLightningFXPacket::renderer,
      ExtraStreamCodecs.VEC3, StaticLightningFXPacket::start,
      ExtraStreamCodecs.VEC3, StaticLightningFXPacket::end,
      ByteBufCodecs.VAR_INT, StaticLightningFXPacket::segments,
      StaticLightningFXPacket::new
  );

  @NotNull
  @Override
  public CustomPacketPayload.Type<StaticLightningFXPacket> type() {
    return TYPE;
  }

  @Override
  public void handle(IPayloadContext context) {
    ClientFXHandlers.createBolt(renderer, preset, start, end, segments);
  }
}