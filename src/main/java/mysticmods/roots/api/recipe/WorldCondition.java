package mysticmods.roots.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.test.world.WorldTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.util.TriPredicate;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.function.Function;
import java.util.function.IntFunction;

public record WorldCondition(String name, Shift shift, WorldTest test,
                             boolean modifyPosition) implements TriPredicate<BlockPos, Level, RandomSource> {
  public static final String ORIGIN = "origin";
  public static final Codec<WorldCondition> CODEC = RecordCodecBuilder.create((codec) -> codec.group(Codec.STRING.fieldOf("name")
      .forGetter((condition) -> condition.name), Shift.CODEC.fieldOf("shift")
      .forGetter((condition) -> condition.shift), WorldTest.CODEC.fieldOf("test")
      .forGetter((condition) -> condition.test), Codec.BOOL.fieldOf("modifyPosition")
      .forGetter((condition) -> condition.modifyPosition)).apply(codec, WorldCondition::new));
  public static final Codec<List<WorldCondition>> LIST_CODEC = CODEC.listOf();
  public static final StreamCodec<RegistryFriendlyByteBuf, WorldCondition> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.STRING_UTF8, o -> o.name, Shift.STREAM_CODEC, o -> o.shift, WorldTest.STREAM_CODEC, o -> o.test, ByteBufCodecs.BOOL, o -> o.modifyPosition, WorldCondition::new);
  public static final StreamCodec<RegistryFriendlyByteBuf, List<WorldCondition>> LIST_STREAM_CODEC = STREAM_CODEC.apply(ByteBufCodecs.list());

  public WorldCondition(WorldTest test) {
    this(ORIGIN, Shift.NONE, test, true);
  }

  public WorldCondition(WorldTest test, boolean modifyPosition) {
    this(ORIGIN, Shift.NONE, test, modifyPosition);
  }

  public WorldCondition(String name, WorldTest test) {
    this(name, Shift.NONE, test, false);
  }

  public WorldCondition(String name, WorldTest test, boolean modifyPosition) {
    this(name, Shift.NONE, test, modifyPosition);
  }

  @Override
  public boolean test(BlockPos blockPos, Level level, RandomSource random) {
    BlockPos pos = shift.apply(blockPos);
    BlockState stateAt = level.getBlockState(pos);
    return test.test(stateAt, random);
  }

  @Nullable
  public BlockPos resolvePosition(BlockPos position) {
    return !modifyPosition ? null : shift == Shift.NONE ? position : shift.apply(position);
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
    public static final IntFunction<Shift> BY_ID = ByIdMap.continuous(Shift::ordinal, Shift.values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, Shift> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Shift::ordinal);

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
