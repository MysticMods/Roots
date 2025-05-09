package mysticmods.roots.network.client.fx;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.network.IRootsPacket;
import mysticmods.roots.client.RenderTickHandler;
import mysticmods.roots.client.particle.BoltEffect;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;
import java.util.function.IntFunction;

public record LightningFXPacket (LightningPreset preset, int renderer, Vec3 start, Vec3 end,
                                    int segments) implements IRootsPacket {
  public LightningFXPacket (LightningPreset preset, int renderer, Vec3 start, Vec3 end) {
    this(preset, renderer, start, end, (int) (Math.sqrt(start.distanceTo(end) * 5)));
  }

  public static final CustomPacketPayload.Type<LightningFXPacket> TYPE = new CustomPacketPayload.Type<>(RootsAPI.rl("fx/lightning"));
  public static final StreamCodec<ByteBuf, LightningFXPacket> CODEC = StreamCodec.composite(
      LightningPreset.STREAM_CODEC, LightningFXPacket::preset,
      ByteBufCodecs.VAR_INT, LightningFXPacket::renderer,
      ExtraStreamCodecs.VEC3, LightningFXPacket::start,
      ExtraStreamCodecs.VEC3, LightningFXPacket::end,
      ByteBufCodecs.VAR_INT, LightningFXPacket::segments,
      LightningFXPacket::new
  );

  @NotNull
  @Override
  public CustomPacketPayload.Type<LightningFXPacket> type() {
    return TYPE;
  }

  @Override
  public void handle(IPayloadContext context) {
    if (preset.shouldAdd.getAsBoolean()) {
      RenderTickHandler.renderBolt(renderer, preset.boltCreator.create(start, end, segments));
    }
  }

  @FunctionalInterface
  public interface BoltCreator {
    BoltEffect create(Vec3 start, Vec3 end, int segments);
  }

  public enum LightningPreset {
    MAGNETIC_ATTRACTION(() -> true, (start, end, segments) ->
        new BoltEffect(BoltEffect.BoltRenderInfo.ELECTRICITY, start, end, segments).size(0.04F).lifespan(8)
            .spawn(BoltEffect.SpawnFunction.noise(8, 4))),
    TOOL_AOE(() -> true, (start, end, segments) ->
        new BoltEffect(BoltEffect.BoltRenderInfo.ELECTRICITY, start, end, segments).size(0.015F).lifespan(12)
            .spawn(BoltEffect.SpawnFunction.NO_DELAY)),
    FANCY(() -> true, (start, end, segments) -> new BoltEffect(BoltEffect.BoltRenderInfo.VINES, start, end, segments).size(0.07f).lifespan(21).spawn(BoltEffect.SpawnFunction.NO_DELAY).fade(BoltEffect.FadeFunction.fade(0.1f)).count(1));

    public static final IntFunction<LightningPreset> BY_ID = ByIdMap.continuous(LightningPreset::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, LightningPreset> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, LightningPreset::ordinal);

    private final BooleanSupplier shouldAdd;
    private final BoltCreator boltCreator;

    LightningPreset(BooleanSupplier shouldAdd, BoltCreator boltCreator) {
      this.shouldAdd = shouldAdd;
      this.boltCreator = boltCreator;
    }
  }
}