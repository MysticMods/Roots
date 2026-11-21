package mysticmods.roots.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mysticmods.roots.api.condition.ICondition;
import mysticmods.roots.api.condition.ILevelCondition;
import mysticmods.roots.api.test.world.PartialBlockStateMatchWorldTest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Collections;
import java.util.Set;

public record BlockStatePropertyCondition(PartialBlockStateMatchWorldTest test) implements ILevelCondition {
  public static final MapCodec<BlockStatePropertyCondition> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(PartialBlockStateMatchWorldTest.CODEC.fieldOf("test")
      .forGetter(BlockStatePropertyCondition::test)).apply(instance, BlockStatePropertyCondition::new));

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
}
