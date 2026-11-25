package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.condition.*;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public record BlockStatePropertyCondition(PartialBlockStateMatchWorldTest test, Optional<Block> supporting) implements ILevelCondition {
  public static final MapCodec<BlockStatePropertyCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(PartialBlockStateMatchWorldTest.CODEC.fieldOf("test")
      .forGetter(BlockStatePropertyCondition::test), BuiltInRegistries.BLOCK.byNameCodec().optionalFieldOf("supporting").forGetter(BlockStatePropertyCondition::supporting)).apply(instance, BlockStatePropertyCondition::new));

  public BlockStatePropertyCondition(PartialBlockStateMatchWorldTest test) {
    this(test, Optional.empty());
  }

  @Override
  public boolean place(Level level, BlockPos position) {
    if (supporting.isPresent()) {
      level.setBlock(position, supporting.get().defaultBlockState(), 3);
      position = position.above();
    }
    level.setBlock(position, test.getBlockState(level.registryAccess()), 3);
    return true;
  }

  @Override
  public Set<BlockPos> test(BlockPos pos, Level level, @javax.annotation.Nullable Player player) {
    if (test.test(level.getBlockState(pos), level.getRandom())) {
      return Collections.singleton(pos.immutable());
    }

    return Collections.emptySet();
  }

  @Override
  public MapCodec<? extends ICondition> codec() {
    return MAP_CODEC;
  }

  @Override
  public List<ConditionIngredient> getIngredient(Holder<ICondition> condition) {
    return ConditionIngredient.fromItemLike(test.getPartialBlockState().block(), condition);
  }
}
