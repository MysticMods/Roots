package mysticmods.roots.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.condition.CanonicalRepresentation;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.condition.ILevelConditionType;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModConditions;
import mysticmods.roots.item.GroveSporesItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Set;

public record OvergrowthCondition() implements ILevelCondition {
  private static final OvergrowthCondition INSTANCE = new OvergrowthCondition();
  public static final MapCodec<OvergrowthCondition> MAP_CODEC = MapCodec.unit(INSTANCE);
  public static final Codec<OvergrowthCondition> CODEC = MAP_CODEC.codec();
  public static final StreamCodec<RegistryFriendlyByteBuf, OvergrowthCondition> STREAM_CODEC = StreamCodec.unit(INSTANCE);

  public static OvergrowthCondition getInstance () {
    return INSTANCE;
  }

  @Override
  public CanonicalRepresentation getRepresentation() {
    return new CanonicalRepresentation(ModBlocks.CREEPING_GROVE_MOSS.get());
  }

  @Override
  public ILevelConditionType<?> type() {
    return ModConditions.OVERGROWTH_CONDITION_TYPE.get();
  }

  @Override
  public Component getName() {
    return Component.translatable("level_condition.roots.overgrowth");
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @Nullable Player player) {
    BlockState state = level.getBlockState(pos);
    boolean creeping = state.is(ModBlocks.CREEPING_GROVE_MOSS.get());
    boolean water = state.getFluidState().isSource() && state.getFluidState().is(FluidTags.WATER);
    if (!water && !creeping) {
      return Collections.emptySet();
    }
    for (Direction dir : Direction.values()) {
      BlockPos offset = pos.above().relative(dir);
      if (water) {
        if (level.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(level, offset, Direction.UP)) {
          return Set.of(pos);
        }
      }
      offset = pos.relative(dir);
      if (creeping) {
        if (level.getFluidState(offset).isEmpty() && GroveSporesItem.canPlace(level, offset, Direction.UP)) {
          return Set.of(pos);
        }
      }
    }

    return Collections.emptySet();
  }

  public static class Type implements ILevelConditionType<OvergrowthCondition> {

    @Override
    public Codec<OvergrowthCondition> codec() {
      return CODEC;
    }

    @Override
    public MapCodec<OvergrowthCondition> mapCodec() {
      return MAP_CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, OvergrowthCondition> streamCodec() {
      return STREAM_CODEC;
    }
  }
}
