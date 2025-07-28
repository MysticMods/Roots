package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.ExtraStreamCodecs;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.init.ModConditions;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record FluidSourcePropertyCondition(String name, TagKey<Fluid> fluidTag) implements ILevelCondition {
  public static final MapCodec<FluidSourcePropertyCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.STRING.fieldOf("name")
      .forGetter(FluidSourcePropertyCondition::name), TagKey.codec(Registries.FLUID).fieldOf("fluidTag")
      .forGetter(FluidSourcePropertyCondition::fluidTag)).apply(instance, FluidSourcePropertyCondition::new));
  public static final Codec<FluidSourcePropertyCondition> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, FluidSourcePropertyCondition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, FluidSourcePropertyCondition::name, ExtraStreamCodecs.FLUID_TAG_STREAM_CODEC, FluidSourcePropertyCondition::fluidTag, FluidSourcePropertyCondition::new);


  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player) {
    FluidState fluid = level.getFluidState(pos);
    if (fluid.is(fluidTag)) {
      return Set.of(pos);
    }
    return Set.of();
  }

  @Override
  public CanonicalRepresentation getRepresentation() {
    var tag = BuiltInRegistries.FLUID.getTag(fluidTag);
    if (tag.isEmpty()) {
      throw new IllegalStateException("Cannot build a canonical representation of " + this + " as the fluid tag " + fluidTag + " is empty");
    }
    return new CanonicalRepresentation(tag.get().get(0).value().defaultFluidState().createLegacyBlock());
  }

  @Override
  public ILevelConditionType<?> type() {
    return ModConditions.FLUID_SOURCE_CONDITION_TYPE.get();
  }

  @Override
  public String getName() {
    return name;
  }

  public static class Type implements ILevelConditionType<FluidSourcePropertyCondition> {

    @Override
    public Codec<FluidSourcePropertyCondition> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<FluidSourcePropertyCondition> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, FluidSourcePropertyCondition> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
