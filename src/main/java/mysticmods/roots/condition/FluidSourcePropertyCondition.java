package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.condition.ILevelCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public record FluidSourcePropertyCondition(TagKey<Fluid> fluidTag) implements ILevelCondition {
  public static final MapCodec<FluidSourcePropertyCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(TagKey.codec(Registries.FLUID)
      .fieldOf("fluidTag")
      .forGetter(FluidSourcePropertyCondition::fluidTag)).apply(instance, FluidSourcePropertyCondition::new));


  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player) {
    FluidState fluid = level.getFluidState(pos);
    if (fluid.is(fluidTag)) {
      return Set.of(pos);
    }
    return Set.of();
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }
}
