package mysticmods.roots.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;

import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Function;

public class WorldCondition implements BiPredicate<BlockPos, Level> {
  private final Shift shift;
  private final RuleTest test;

  public static final Codec<WorldCondition> CODEC = RecordCodecBuilder.create((codec) -> codec.group(Shift.CODEC.fieldOf("shift").forGetter((condition) -> condition.shift), RuleTest.CODEC.fieldOf("test").forGetter((condition) -> condition.test)).apply(codec, WorldCondition::new));

  public WorldCondition(Shift shift, RuleTest test) {
    this.shift = shift;
    this.test = test;
  }

  public WorldCondition(RuleTest test) {
    this(Shift.NONE, test);
  }

  @Override
  public boolean test(BlockPos blockPos, Level level) {
    BlockPos pos = shift.apply(blockPos);
    BlockState stateAt = level.getBlockState(pos);
    return test.test(stateAt, level.getRandom());
  }

  public enum Shift implements Function<BlockPos, BlockPos>, StringRepresentable {
    NONE(null),
    ABOVE(Direction.UP),
    BELOW(Direction.DOWN),
    NORTH(Direction.NORTH),
    SOUTH(Direction.SOUTH),
    EAST(Direction.EAST),
    WEST(Direction.WEST);

    public static final Codec<Shift> CODEC = StringRepresentable.fromEnum(Shift::values);

    private final Direction offset;

    Shift(Direction offset) {
      this.offset = offset;
    }

    @Override
    public BlockPos apply(BlockPos blockPos) {
      return offset == null ? blockPos : blockPos.relative(offset);
    }

    @Override
    public String getSerializedName() {
      return this.name().toLowerCase(Locale.ROOT);
    }
  }
}
