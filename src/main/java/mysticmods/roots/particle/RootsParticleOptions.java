package mysticmods.roots.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.RootsAPI;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public record RootsParticleOptions(ParticleType<?> type, int color1, int color2,
                                   int entityId, int casterId, int fastForward,
                                   @Nullable ItemStack item, @Nullable BlockPos pos, int delay) implements ParticleOptions {

  private static final Codec<ItemStack> ITEM_CODEC = Codec.withAlternative(ItemStack.SINGLE_ITEM_CODEC, ItemStack.ITEM_NON_AIR_CODEC, ItemStack::new);
  private static final Codec<double[]> DOUBLE_ARRAY_CODEC = Codec.DOUBLE.listOf().xmap(
      list -> list.stream().mapToDouble(Double::doubleValue).toArray(),
      array -> Arrays.stream(array).boxed().toList());
  private static final StreamCodec<ByteBuf, double[]> DOUBLE_ARRAY_STREAM_CODEC = ByteBufCodecs.DOUBLE.apply(ByteBufCodecs.list()).map(
      list -> list.stream().mapToDouble(Double::doubleValue).toArray(),
      array -> Arrays.stream(array).boxed().toList());

  public static MapCodec<RootsParticleOptions> codec(ParticleType<?> type) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color1").forGetter(RootsParticleOptions::color1),
        Codec.INT.fieldOf("color2").forGetter(RootsParticleOptions::color2),
        Codec.INT.fieldOf("entityId").forGetter(RootsParticleOptions::entityId),
        Codec.INT.fieldOf("casterId").forGetter(RootsParticleOptions::casterId),
        Codec.INT.fieldOf("fastForward").forGetter(RootsParticleOptions::fastForward),
        ITEM_CODEC.optionalFieldOf("item").forGetter(o -> Optional.ofNullable(o.item())),
        BlockPos.CODEC.optionalFieldOf("pos").forGetter(o -> Optional.ofNullable(o.pos())),
        Codec.INT.fieldOf("decay").forGetter(RootsParticleOptions::delay)
    ).apply(instance, (a, b, c, d, e, f, g, h) -> new RootsParticleOptions(type, a, b, c, d, e, f.orElse(null), g.orElse(null), h)));
  }

  public static StreamCodec<RegistryFriendlyByteBuf, RootsParticleOptions> streamCodec(ParticleType<?> type) {
    return ExtraStreamCodecs.composite(
        ByteBufCodecs.VAR_INT, RootsParticleOptions::color1,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::color2,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::entityId,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::casterId,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::fastForward,
        ByteBufCodecs.optional(ItemStack.STREAM_CODEC), o -> Optional.ofNullable(o.item()),
        ByteBufCodecs.optional(BlockPos.STREAM_CODEC), o -> Optional.ofNullable(o.pos()),
        ByteBufCodecs.VAR_INT, RootsParticleOptions::delay,
        (c1, c2, e, f, g, h, i, j) -> new RootsParticleOptions(type, c1, c2, e, f, g, h.orElse(null), i.orElse(null), j)
    );
  }

  public RootsParticleOptions swapColors(RandomSource random) {
    return builder().swapColors(random).build();
  }

  public RootsParticleOptions swapColors() {
    return builder().swapColors().build();
  }

  public Builder builder() {
    return new Builder(type)
        .color(color1, color2)
        .entityId(entityId)
        .casterId(casterId)
        .fastForward(fastForward)
        .item(item)
        .pos(pos)
        .delay(delay);
  }

  public static Builder builder(ParticleType<?> type) {
    return new Builder(type);
  }

  public static Builder builder(DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> type) {
    return new Builder(type.get());
  }

  public static class Builder {
    private ParticleType<?> type;
    private int color1 = 0xffffff, color2 = 0xffffff,
        entityId, casterId, fastForward, delay;
    private ItemStack item = null;
    private BlockPos pos = null;
    private double[] spawn = new double[]{0.0, 0.0, 0.0};
    private double[] velocity = new double[]{0.0, 0.0, 0.0};
    private boolean forceSpawn = false;

    public Builder(ParticleType<?> type) {
      this.type = type;
    }

    public Builder type(ParticleType<?> type) {
      this.type = type;
      return this;
    }

    public Builder delay (int delay) {
      this.delay = delay;
      return this;
    }

    public Builder swapColors() {
      int temp = this.color1;
      this.color1 = this.color2;
      this.color2 = temp;
      return this;
    }

    public Builder spawn (double[] spawn) {
      if (spawn.length != 3) {
        throw new IllegalArgumentException("Spawn array must have exactly 3 elements, got: " + spawn.length);
      }
      this.spawn = spawn;
      return this;
    }

    public Builder x (double x) {
      this.spawn[0] = x;
      return this;
    }

    public Builder y (double y) {
      this.spawn[1] = y;
      return this;
    }

    public Builder z (double z) {
      this.spawn[2] = z;
      return this;
    }

    public Builder velocity (double[] velocity) {
      if (velocity.length != 3) {
        throw new IllegalArgumentException("Velocity array must have exactly 3 elements, got: " + velocity.length);
      }
      this.velocity = velocity;
      return this;
    }

    public Builder vx (double vx) {
      this.velocity[0] = vx;
      return this;
    }

    public Builder vy (double vy) {
      this.velocity[1] = vy;
      return this;
    }

    public Builder vz (double vz) {
      this.velocity[2] = vz;
      return this;
    }

    public Builder forceSpawn() {
      this.forceSpawn = true;
      return this;
    }

    public Builder swapColors(RandomSource random) {
      if (random.nextBoolean()) {
        return swapColors();
      }
      return this;
    }

    public Builder item(@Nullable ItemStack item) {
      this.item = item;
      return this;
    }

    public Builder color(Holder<Spell> spell) {
      return color(spell.value().getColor1(), spell.value().getColor2());
    }

    public Builder type(DeferredHolder<ParticleType<?>, ParticleType<RootsParticleOptions>> type) {
      this.type = type.get();
      return this;
    }

    public Builder color(int color) {
      this.color1 = color;
      this.color2 = color;
      return this;
    }

    public Builder color(int color1, int color2) {
      this.color1 = color1;
      this.color2 = color2;
      return this;
    }

    public Builder color (int[] color) {
      if (color.length == 1) {
        return this.color(color[0]);
      } else if (color.length == 2) {
        return this.color(color[0], color[1]);
      } else {
        throw new IllegalArgumentException("Color array must have 1 or 2 elements, got: " + color.length);
      }
    }

    public Builder entityId(int entityId) {
      this.entityId = entityId;
      return this;
    }

    public Builder casterId(int casterId) {
      this.casterId = casterId;
      return this;
    }

    public Builder fastForward(int fastForward) {
      this.fastForward = fastForward;
      return this;
    }

    public Builder pos(@Nullable BlockPos pos) {
      this.pos = pos;
      return this;
    }

    @Deprecated
    public RootsParticleOptions build() {
      return new RootsParticleOptions(type, color1, color2, entityId, casterId, fastForward, item, pos, delay);
    }

    public void build (Level level) {
      if (spawn[0] == 0.0 && spawn[1] == 0.0 && spawn[2] == 0.0) {
        RootsAPI.LOG.error("Attempted to spawn particle with zero spawn coordinates. Was this intentional? Particle: {}", this);
      }
      level.addParticle(build(), forceSpawn, spawn[0], spawn[1], spawn[2], velocity[0], velocity[1], velocity[2]);
    }
  }

  @Override
  public ParticleType<?> getType() {
    return type;
  }

  public static class Type extends ParticleType<RootsParticleOptions> {
    public Type(boolean overrideLimitter) {
      super(overrideLimitter);
    }

    @Override
    public MapCodec<RootsParticleOptions> codec() {
      return RootsParticleOptions.codec(this);
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, RootsParticleOptions> streamCodec() {
      return RootsParticleOptions.streamCodec(this);
    }
  }
}
