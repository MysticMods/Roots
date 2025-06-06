package mysticmods.roots.particle;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.spell.Spell;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public record RootsParticleOptions(ParticleType<?> type, int color1, int color2,
                                   int entityId, int casterId, int fastForward,
                                   @Nullable ItemStack item) implements ParticleOptions {

  private static final Codec<ItemStack> ITEM_CODEC = Codec.withAlternative(ItemStack.SINGLE_ITEM_CODEC, ItemStack.ITEM_NON_AIR_CODEC, ItemStack::new);

  public static MapCodec<RootsParticleOptions> codec(ParticleType<?> type) {
    return RecordCodecBuilder.mapCodec(instance -> instance.group(
        Codec.INT.fieldOf("color1").forGetter(RootsParticleOptions::color1),
        Codec.INT.fieldOf("color2").forGetter(RootsParticleOptions::color2),
        Codec.INT.fieldOf("entityId").forGetter(RootsParticleOptions::entityId),
        Codec.INT.fieldOf("casterId").forGetter(RootsParticleOptions::casterId),
        Codec.INT.fieldOf("fastForward").forGetter(RootsParticleOptions::fastForward),
        ITEM_CODEC.optionalFieldOf("item").forGetter(o -> Optional.ofNullable(o.item()))
    ).apply(instance, (a, b, c, d, e, f) -> new RootsParticleOptions(type, a, b, c, d, e, f.orElse(null))));
  }

  public static StreamCodec<RegistryFriendlyByteBuf, RootsParticleOptions> streamCodec(ParticleType<?> type) {
    return StreamCodec.composite(
        ByteBufCodecs.VAR_INT, RootsParticleOptions::color1,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::color2,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::entityId,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::casterId,
        ByteBufCodecs.VAR_INT, RootsParticleOptions::fastForward,
        ByteBufCodecs.optional(ItemStack.STREAM_CODEC), o -> Optional.ofNullable(o.item()),
        (c1, c2, e, f, g, h) -> new RootsParticleOptions(type, c1, c2, e, f, g, h.orElse(null))
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
        .fastForward(fastForward);
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
        entityId, casterId, fastForward;
    private ItemStack item = null;

    public Builder(ParticleType<?> type) {
      this.type = type;
    }

    public Builder type(ParticleType<?> type) {
      this.type = type;
      return this;
    }

    public Builder swapColors() {
      int temp = this.color1;
      this.color1 = this.color2;
      this.color2 = temp;
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

    public RootsParticleOptions build() {
      return new RootsParticleOptions(type, color1, color2, entityId, casterId, fastForward, item);
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
