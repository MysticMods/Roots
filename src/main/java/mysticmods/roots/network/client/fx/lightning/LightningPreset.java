package mysticmods.roots.network.client.fx.lightning;

import io.netty.buffer.ByteBuf;
import mysticmods.roots.client.particle.bolt.BoltEffect;
import mysticmods.roots.client.particle.bolt.BoltRenderInfo;
import mysticmods.roots.client.particle.bolt.FadeFunction;
import mysticmods.roots.client.particle.bolt.SpawnFunction;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;

import java.util.function.BooleanSupplier;
import java.util.function.IntFunction;

public enum LightningPreset {
  MAGNETIC_ATTRACTION(() -> true, (provider, segments) ->
      BoltCreator.createBolt(BoltRenderInfo.ELECTRICITY, provider, segments).size(0.04F).lifespan(8)
          .spawn(SpawnFunction.noise(8, 4))),
  TOOL_AOE(() -> true, (provider, segments) ->
      BoltCreator.createBolt(BoltRenderInfo.ELECTRICITY, provider, segments).size(0.015F).lifespan(12)
          .spawn(SpawnFunction.NO_DELAY)),
  DISARM(() -> true, (provider, segments) -> BoltCreator.createBolt(BoltRenderInfo.VINES, provider, segments).size(0.02f)
      .lifespan(9).spawn(SpawnFunction.NO_DELAY).fade(FadeFunction.fade(0.3f))),
  SHATTER(() -> true, (provider, segments) -> BoltCreator.createBolt(BoltRenderInfo.shatter(), provider, segments).size(0.01f)
      .lifespan(26).spawn(SpawnFunction.NO_DELAY).fade(FadeFunction.fade(0.1f, 0.3f)));

  public static final IntFunction<LightningPreset> BY_ID = ByIdMap.continuous(LightningPreset::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
  public static final StreamCodec<ByteBuf, LightningPreset> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, LightningPreset::ordinal);

  private final BooleanSupplier shouldAdd;
  private final BoltCreator boltCreator;

  LightningPreset(BooleanSupplier shouldAdd, BoltCreator boltCreator) {
    this.shouldAdd = shouldAdd;
    this.boltCreator = boltCreator;
  }

  public BoltCreator getBoltCreator() {
    return boltCreator;
  }

  public BooleanSupplier getShouldAdd() {
    return shouldAdd;
  }
}
