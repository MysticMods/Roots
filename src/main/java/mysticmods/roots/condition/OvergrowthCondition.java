package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import mysticmods.roots.api.condition.*;
import mysticmods.roots.init.ModBlocks;
import mysticmods.roots.init.ModItems;
import mysticmods.roots.item.GroveSporesItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record OvergrowthCondition() implements ILevelCondition {
  private static final OvergrowthCondition INSTANCE = new OvergrowthCondition();
  public static final MapCodec<OvergrowthCondition> MAP_CODEC = MapCodec.unit(INSTANCE);

  public static OvergrowthCondition getInstance() {
    return INSTANCE;
  }

  @Override
  public boolean place(Level level, BlockPos position) {
    level.setBlock(position, ModBlocks.CREEPING_GROVE_MOSS.value().defaultBlockState(), 3);
    return true;
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

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public List<ConditionIngredient> getIngredient(Holder<ICondition> condition) {
    List<ConditionIngredient> ingredients = new ArrayList<>();
    ingredients.add(new ConditionIngredient(condition, new ItemStack(ModItems.OVERGROWTH_CONDITION)));
    ingredients.add(new ConditionIngredient(condition, new ItemStack(ModItems.CREEPING_GROVE_MOSS)));
    return ingredients;
  }
}
