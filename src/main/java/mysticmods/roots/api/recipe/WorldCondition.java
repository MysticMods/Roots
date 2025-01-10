package mysticmods.roots.api.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import mysticmods.roots.api.registry.RootsRegistries;
import mysticmods.roots.api.world.WorldTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;
import net.neoforged.neoforge.common.util.TriPredicate;
import net.neoforged.neoforge.registries.GameData;

import java.util.Locale;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.IntFunction;

public record WorldCondition(Shift shift, WorldTest test) implements TriPredicate<BlockPos, Level, RandomSource> {
  public static final Codec<WorldCondition> CODEC = RecordCodecBuilder.create((codec) -> codec.group(Shift.CODEC.fieldOf("shift").forGetter((condition) -> condition.shift), WorldTest.CODEC.fieldOf("test").forGetter((condition) -> condition.test)).apply(codec, WorldCondition::new));
  public static final StreamCodec<RegistryFriendlyByteBuf, WorldCondition> STREAM_CODEC = StreamCodec.composite(Shift.STREAM_CODEC, o -> o.shift, WorldTest.STREAM_CODEC, o -> o.test, WorldCondition::new);

  public WorldCondition(WorldTest test) {
    this(Shift.NONE, test);
  }

  @Override
  public boolean test(BlockPos blockPos, Level level, RandomSource random) {
    BlockPos pos = shift.apply(blockPos);
    BlockState stateAt = level.getBlockState(pos);
    return test.test(stateAt, random);
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
